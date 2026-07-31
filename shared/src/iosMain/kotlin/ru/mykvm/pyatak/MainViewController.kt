package ru.mykvm.pyatak

import androidx.compose.ui.window.ComposeUIViewController
import di.utilsModule
import org.koin.core.context.startKoin
import ru.mykvm.language.imp.di.languageModule
import ru.mykvm.language.imp.di.languagePlatformModule
import ru.mykvm.splash.imp.di.splashModule

//fun MainViewController() = ComposeUIViewController { App() }

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                utilsModule,
//                networkModule,
//                storagePlatformModule,
//                storageModule,
//                networkConnectionModule,
                languagePlatformModule,
                languageModule,
                splashModule,
//                asModeModule,
            )
        }
    }
) {
    App()
}