package ru.mykvm.pyatak

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

import ru.mykvm.language.api.domain.usecase.GetCurrentLanguageUseCase
import ru.mykvm.language.api.model.CurrentLanguageDomain
import ru.mykvm.pyatak.base.navigation.NavigationApp
import ru.mykvm.pyatak.base.engine.theme.AppTheme
import ru.mykvm.pyatak.base.utils.model.ResultState

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