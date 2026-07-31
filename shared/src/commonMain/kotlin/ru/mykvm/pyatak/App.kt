package ru.mykvm.pyatak

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

import pyatak.shared.generated.resources.Res
import pyatak.shared.generated.resources.compose_multiplatform
import ru.mykvm.language.api.domain.usecase.GetCurrentLanguageUseCase
import ru.mykvm.language.api.model.CurrentLanguageDomain
import ru.mykvm.navigation.NavigationApp
import ru.mykvm.ui.theme.AppTheme
import ru.mykvm.utils.model.ResultState

//@Composable
//@Preview
//fun App() {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}

@Composable
@Preview
fun App() {
    var langIsRus by remember { mutableStateOf(false) }
    val getCurrentLanguageUseCase = koinInject<GetCurrentLanguageUseCase>()
    LaunchedEffect(key1 = Unit) {
        getCurrentLanguageUseCase.getLang()
        getCurrentLanguageUseCase.langState.collect { state->
            if(state is ResultState.Success){
                if(state.data == CurrentLanguageDomain.RU){
                    langIsRus = true
                }
            }
        }
    }
    AppTheme {
        NavigationApp(langIsRus = langIsRus)
    }
}