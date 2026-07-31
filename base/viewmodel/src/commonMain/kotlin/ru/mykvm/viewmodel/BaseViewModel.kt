package ru.mykvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mykvm.utils.closeApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax
import ru.mykvm.network.model.NetworkError
import ru.mykvm.utils.model.BaseErrors

abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any>(
    initialState: STATE
) : ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when(throwable) {
            is NetworkError.BadRequest, is NetworkError.Unauthorized -> {
                // Логика авторизации
            }
            else -> {
                // Остальные ошибки
            }
        }
    }

    private val scopeWithHandler: CoroutineScope = viewModelScope + coroutineExceptionHandler

    override val container = scopeWithHandler.container<STATE, SIDE_EFFECT>(initialState)

    protected fun orbitIntent(
        transform: suspend Syntax<STATE, SIDE_EFFECT>.() -> Unit
    ) = intent {
        transform()
    }

    protected fun postErrorSideEffect(error: BaseErrors) = intent {
        postSideEffect(error as SIDE_EFFECT)
    }

    abstract fun intentHandler(intent: Any)

    fun finishApplication() = closeApp()
}