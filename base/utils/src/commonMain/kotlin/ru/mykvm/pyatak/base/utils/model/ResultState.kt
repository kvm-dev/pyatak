package ru.mykvm.pyatak.base.utils.model

sealed class ResultState<out T> {
    data object Idle : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T?) : ResultState<T>()
}