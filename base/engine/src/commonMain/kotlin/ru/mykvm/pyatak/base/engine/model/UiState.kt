package ru.mykvm.pyatak.base.engine.model

 sealed class UiState<out T> {
    data object Empty : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T?) : UiState<T>()
}