package ru.mykvm.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.pyatakLogo

@Composable
fun LoadingLogo(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Image(
        painter = painterResource(Res.drawable.pyatakLogo),
        contentDescription = null,
        modifier = modifier
            .size(100.dp)
            .graphicsLayer {
                rotationZ = rotationAngle
            },

    )
}