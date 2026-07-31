package ru.mykvm.ui.model

 sealed class UiState<out T> {
    data object Empty : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T?) : UiState<T>()
}