package ru.mykvm.pyatak

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import di.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.mykvm.language.imp.di.languageModule
import ru.mykvm.language.imp.di.languagePlatformModule
import ru.mykvm.splash.imp.di.splashModule

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            App()
//        }
//    }
//}
//
//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        startKoin {
            // Provide the Android Context to Koin
            androidContext(this@MainActivity)
            androidLogger(Level.DEBUG) // Optional: for logging Koin activities
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}