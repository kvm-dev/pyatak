package ru.mykvm.splash.imp.presentation.viewmodel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import ru.mykvm.language.api.model.CurrentLanguageDomain
import ru.mykvm.splash.imp.domain.interactor.SplashScreenInteractor
import ru.mykvm.splash.imp.presentation.ui.SplashScreenIntents
import ru.mykvm.splash.imp.presentation.ui.SplashScreenSideEffects
import ru.mykvm.splash.imp.presentation.ui.SplashScreenViewState
import ru.mykvm.viewmodel.BaseViewModel

class SplashScreenViewModel(private val interactor: SplashScreenInteractor) : BaseViewModel<SplashScreenViewState, SplashScreenSideEffects>(
    SplashScreenViewState.Idle
) {
    private fun loadData() = orbitIntent {
//        val connection = interactor.isNetworkAvailable()
//        interactor.getCurrentLang()
//        interactor.langState.zip(interactor.userToken) { langState, userToken ->
//            println("token is $userToken")
//            Pair(langState, userToken)
//        }.collectLatest { result ->
//            if (result.first is ResultState.Success) {
//                if (result.second.isNotEmpty()) {
//                    if (connection) {
//                        interactor.getProfile()
//                    } else {
//                        interactor.getProfileFromLocal()
//                    }
//
//                    delay(5000L)
//                    postSideEffect(SplashScreenSideEffects.NAVIGATE_TO_AUTHORIZED_ZONE_SIDE_EFFECT)
//                } else {
//                    delay(5000L)
//                    postSideEffect(SplashScreenSideEffects.NAVIGATE_TO_AUTHORIZATION_ZONE_SIDE_EFFECT)
//                }
//            }
//            reduce { SplashScreenViewState.Loading }
//        }
        reduce { SplashScreenViewState.Loading }
    }

//    fun getLang(): CurrentLanguageDomain {
//        val langState = interactor.langState.value
//        return if (langState is ResultState.Success) {
//            langState.data ?: interactor.getCurrentLang()
//        } else {
//            interactor.getCurrentLang()
//        }
//    }

    override fun intentHandler(intent: Any) {
        when(intent){
            SplashScreenIntents.InitViewModelIntent -> loadData()
        }
    }
}
