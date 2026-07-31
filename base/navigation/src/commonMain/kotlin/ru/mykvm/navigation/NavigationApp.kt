package ru.mykvm.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import ru.mykvm.splash.imp.presentation.ui.SplashScreen
import ru.mykvm.ui.theme.AppTheme
import ru.mykvm.utils.eventBusAndNavigationGraph.AppDestinations
import ru.mykvm.utils.eventBusAndNavigationGraph.EventBus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(langIsRus: Boolean) {
    val eventBus = koinInject<EventBus>()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var withSplashBackground by remember { mutableStateOf(true) }
    LaunchedEffect(currentDestination) {
        currentDestination?.let { dest ->
            if (dest.hasRoute<AppDestinations.Main>() || dest.hasRoute<AppDestinations.Authorization>()) {
                withSplashBackground = false
            }
        }
    }
    val modifier = Modifier.fillMaxSize()
//    val modifier = if (withSplashBackground) {
//        Modifier
//            .fillMaxSize()
//            .background(brush = Brush.verticalGradient(colors = listOf(AppTheme.colors.mainOrangeLight, AppTheme.colors.turquoise)))
//    } else {
//        Modifier
//            .fillMaxSize()
//            .paint(painter = background, contentScale = ContentScale.Crop, alignment = Alignment.Center, sizeToIntrinsics = true)
//    }

    Scaffold(
        containerColor = Color.Transparent,
    ) { innerPadding ->
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = AppDestinations.Splash
        ) {
//            composable<AppDestinations.Main> {
//                val mainViewModel = koinInject<MainScreenViewModel>()
//                MainScreen(
//                    modifier = Modifier.fillMaxSize().padding(innerPadding),
//                    viewModel = mainViewModel,
//                    onNavigationAuthorization = {
//                        navController.navigate(route = AppDestinations.Authorization) {
//                            popUpTo(AppDestinations.Splash) { inclusive = true }
//                        }
//                    },
//                    navController = navController,
//                    eventDestination = AppDestinations.EventsInner(),
//                    onClickEvents = {
//                        navController.navigate(route = AppDestinations.EventsList)
//                    },
//                    onClickBooks = {
//                        navController.navigate(route = AppDestinations.BooksList)
//                    },
//                    onClickStudy = {
//                        navController.navigate(route = AppDestinations.StudyList)
//                    }
//                )
//            }
            composable<AppDestinations.Splash> {
                SplashScreen(onNavigateToHome = {
                    navController.navigate(route = AppDestinations.Main) {
                        popUpTo(AppDestinations.Splash) { inclusive = true }
                    }},
                    onNavigationAuthorization = {
                        navController.navigate(route = AppDestinations.Authorization) {
                            popUpTo(AppDestinations.Splash) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
            }
        }
    }
}