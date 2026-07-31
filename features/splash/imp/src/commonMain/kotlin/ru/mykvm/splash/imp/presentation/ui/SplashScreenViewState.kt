package ru.mykvm.splash.imp.presentation.ui

sealed class SplashScreenViewState{
    data object Idle : SplashScreenViewState()
    data object Loading : SplashScreenViewState()
}
