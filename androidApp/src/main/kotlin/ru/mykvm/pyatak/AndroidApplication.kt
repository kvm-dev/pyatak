package ru.mykvm.pyatak

import android.app.Application
import ru.mykvm.pyatak.base.utils.di.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.mykvm.language.imp.di.languageModule
import ru.mykvm.language.imp.di.languagePlatformModule
import ru.mykvm.splash.imp.di.splashModule
import ru.mykvm.pyatak.base.engine.di.enginePlatformModule

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AndroidApplication)
                modules(
                    utilsModule,
//                networkModule,
//                storagePlatformModule,
//                storageModule,
//                networkConnectionModule,
                    languagePlatformModule,
                    languageModule,
                    splashModule,
                    enginePlatformModule,
//                asModeModule,
                )
            }
        }
    }