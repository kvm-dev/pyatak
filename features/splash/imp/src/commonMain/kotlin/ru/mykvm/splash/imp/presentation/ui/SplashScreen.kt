package ru.mykvm.splash.imp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mykvm.splash.imp.presentation.viewmodel.SplashScreenViewModel
import ru.mykvm.ui.creatures.hauntsman.Huntsman
import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
import ru.mykvm.ui.tiles.TestMap

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
            TestMap()
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                AppTheme.colors.mainSplashDark,
//                                AppTheme.colors.secondSplashDark
//                            )
//                        )
//                    )
//            ) {
//
//                Image(
//                    painter = painterResource(drawable.background),
//                    contentDescription = "background",
//                    modifier = Modifier.fillMaxSize(), // Растягиваем на весь экран
//                    contentScale = ContentScale.Crop   // Заполняем экран, мягко обрезая края при необходимости
//                )
//                FlyStarship(modifier = Modifier.width(160.dp).padding(start = 4.dp, top = 6.dp))
//                LoadingLogo(modifier = Modifier.align(Alignment.BottomEnd))
//            }
        }
    }
}
