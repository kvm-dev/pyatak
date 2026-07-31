package ru.mykvm.splash.imp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mykvm.splash.imp.ComposeResources.Res.drawable
import ru.mykvm.splash.imp.ComposeResources.background
import ru.mykvm.splash.imp.presentation.viewmodel.SplashScreenViewModel
import ru.mykvm.ui.components.FlyStarship
import ru.mykvm.ui.components.LoadingLogo
import ru.mykvm.ui.theme.AppTheme

@Composable
fun SplashScreen(viewModel: SplashScreenViewModel = koinViewModel(), onNavigateToHome: () -> Unit, onNavigationAuthorization: () -> Unit, modifier: Modifier) {

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SplashScreenSideEffects.NAVIGATE_TO_AUTHORIZED_ZONE_SIDE_EFFECT -> { onNavigateToHome() }
            SplashScreenSideEffects.NAVIGATE_TO_AUTHORIZATION_ZONE_SIDE_EFFECT -> { onNavigationAuthorization() }
        }
    }

    when(state){
        SplashScreenViewState.Idle -> {
            viewModel.intentHandler(SplashScreenIntents.InitViewModelIntent)
        }

        SplashScreenViewState.Loading-> {
//            val composition by rememberLottieComposition {
//                LottieCompositionSpec.JsonString(
//                    Res.readBytes("files/fs-animation.json").decodeToString()
//                )
//            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AppTheme.colors.mainSplashDark,
                                AppTheme.colors.secondSplashDark
                            )
                        )
                    )
            ) {

                Image(
                    painter = painterResource(drawable.background),
                    contentDescription = "background",
                    modifier = Modifier.fillMaxSize(), // Растягиваем на весь экран
                    contentScale = ContentScale.Crop   // Заполняем экран, мягко обрезая края при необходимости
                )
                FlyStarship(modifier = Modifier.width(160.dp).padding(start = 4.dp, top = 6.dp))
                LoadingLogo(modifier = Modifier.align(Alignment.BottomEnd))

//                LogoIcon(
//                    modifier = Modifier
//                        .size(128.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//                Text(
//                    getSplashDescription(lang = (viewModel.getLang())),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 40.dp),
//                    style = AppTheme.typography.splashDescription
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Image(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    contentScale = ContentScale.FillWidth,
//                    painter = rememberLottiePainter(
//                        composition = composition,
//                        iterations = Compottie.IterateForever),
//                    contentDescription = ""
//                )
            }
        }
    }
}
