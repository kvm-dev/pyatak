package ru.mykvm.splash.imp.presentation.ui

sealed class SplashScreenIntents {
    data object InitViewModelIntent : SplashScreenIntents()
}