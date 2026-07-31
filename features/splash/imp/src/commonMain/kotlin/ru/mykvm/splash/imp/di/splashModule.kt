package ru.mykvm.splash.imp.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.mykvm.splash.imp.domain.interactor.SplashScreenInteractor
import ru.mykvm.splash.imp.presentation.viewmodel.SplashScreenViewModel

val splashModule = module {
    single<SplashScreenInteractor> { SplashScreenInteractor() }
    viewModelOf(::SplashScreenViewModel)
}