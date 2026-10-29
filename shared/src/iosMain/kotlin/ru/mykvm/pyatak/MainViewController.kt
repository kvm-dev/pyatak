package ru.mykvm.pyatak

import androidx.compose.ui.window.ComposeUIViewController
import ru.mykvm.pyatak.base.utils.di.utilsModule
import org.koin.core.context.startKoin
import ru.mykvm.language.imp.di.languageModule
import ru.mykvm.language.imp.di.languagePlatformModule
import ru.mykvm.splash.imp.di.splashModule
import ru.mykvm.pyatak.base.engine.di.enginePlatformModule

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
                enginePlatformModule
//                asModeModule,
            )
        }
    }
) {
    App()
}