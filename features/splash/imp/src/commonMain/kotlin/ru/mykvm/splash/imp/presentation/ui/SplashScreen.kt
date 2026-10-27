package ru.mykvm.splash.imp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mykvm.splash.imp.presentation.viewmodel.SplashScreenViewModel
import ru.mykvm.ui.hero.HeroType
import ru.mykvm.ui.levels.TestMap

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
            TestMap(
                initialTeamClasses = listOf(
                    HeroType.SHAMAN,
                    HeroType.BOY_LIGHTSABER,
                    HeroType.DOCTOR
                ),
                onGameOver = {
                    // 💀 Сработает, если вся тройка погибла
                    println("Игра Окончена: команда полностью разбита!")
                    // Здесь можно открыть экран поражения или сделать рестарт
                },
                onLevelComplete = { resources, deadHeroes ->
                    // 🎉 Сработает, когда на карте не осталось живых врагов
                    println("Уровень успешно завершен!")

                    // Пример обработки собранных ресурсов:
                    resources.forEach { (resourceType, count) ->
                        println("Добыто ресурса ${resourceType.name}: $count шт.")
                    }

                    // Пример обработки потерь:
                    if (deadHeroes.isEmpty()) {
                        println("Идеально! Вся команда выжила.")
                    } else {
                        println("Потери в бою: погибшие классы -> ${deadHeroes.joinToString { it.displayName }}")
                    }

                    // Здесь можно начислить награды в профиль и загрузить следующую карту
                }
            )
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
