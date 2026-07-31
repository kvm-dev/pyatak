package ru.mykvm.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.resources.painterResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.pigStarShip

@Composable
fun FlyStarship(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val translationY by infiniteTransition.animateFloat(
        initialValue = -20f, // Верхняя точка левитации
        targetValue = 20f,   // Нижняя точка левитации
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Image(
        painter = painterResource(Res.drawable.pigStarShip),
        contentDescription = null,
        modifier = modifier
            .graphicsLayer {
                this.translationY = translationY
            }
    )
}