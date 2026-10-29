package ru.mykvm.pyatak.base.engine.hero

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.imageResource

@Composable
fun HeroRenderer(
    heroState: HeroState,
    currentFrame: Int,
    modifier: Modifier = Modifier
) {
    val config = heroState.currentConfig
    val sprite = imageResource(config.drawable)

    Canvas(modifier = modifier) {
        val frameWidth = 256
        val frameHeight = 256

        // Защита от выхода за границы массива кадров
        val safeFrame = currentFrame.coerceIn(0, config.frameCount - 1)
        val row = safeFrame / config.framesPerRow
        val col = safeFrame % config.framesPerRow

        val srcOffset = IntOffset(x = col * frameWidth, y = row * frameHeight)
        val srcSize = IntSize(frameWidth, frameHeight)
        val dstSize = IntSize(size.width.toInt(), size.height.toInt())

        withTransform({
            if (heroState.isLeft) {
                scale(scaleX = -1f, scaleY = 1f, pivot = center)
            }
        }) {
            drawImage(
                image = sprite,
                srcOffset = srcOffset,
                srcSize = srcSize,
                dstOffset = IntOffset.Zero,
                dstSize = dstSize,
                filterQuality = FilterQuality.None
            )
        }
    }
}