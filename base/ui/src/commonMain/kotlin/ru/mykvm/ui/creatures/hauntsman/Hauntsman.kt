package ru.mykvm.ui.creatures.hauntsman

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import ru.mykvm.ui.ComposeResources.Res
import ru.mykvm.ui.ComposeResources.huntsmanAttackRifle
import ru.mykvm.ui.ComposeResources.huntsmanIdle
import ru.mykvm.ui.ComposeResources.huntsmanWalk
import ru.mykvm.ui.utils.coerceInBox
import kotlin.math.ceil

@Composable
fun Huntsman(
    drawableResource: DrawableResource,
    frameCount: Int,
    framesPerRow: Int,
    durationMillis: Int,
    isLeft: Boolean = false,
    loop: Boolean = true,
    onAnimationEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Безопасное получение изображения в Compose Multiplatform
    val sprite = imageResource(drawableResource)
    val frameAnimatable = remember { Animatable(0f) }

    LaunchedEffect(drawableResource, frameCount, loop) {
        frameAnimatable.snapTo(0f)
        if (loop) {
            frameAnimatable.animateTo(
                targetValue = (frameCount - 1).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            frameAnimatable.animateTo(
                targetValue = (frameCount - 1).toFloat(),
                animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
            )
            onAnimationEnd() // Сигнал о завершении анимации (например, атаки)
        }
    }

    Canvas(modifier = modifier) {
        // Жестко задаем ваш размер кадра в исходном файле
        val frameWidth = 256
        val frameHeight = 256

        val currentFrame = frameAnimatable.value.toInt().coerceIn(0, frameCount - 1)
        val row = currentFrame / framesPerRow
        val col = currentFrame % framesPerRow

        // Координаты вырезки кадра из текстуры (в пикселях)
        val srcOffset = IntOffset(x = col * frameWidth, y = row * frameHeight)
        val srcSize = IntSize(frameWidth, frameHeight)

        // Размер отрисовки на экране (подгоняется под размер Canvas/Box)
        val dstSize = IntSize(size.width.toInt(), size.height.toInt())

        withTransform({
            if (isLeft) {
                // Разворачиваем персонажа влево относительно его центра
                scale(scaleX = -1f, scaleY = 1f, pivot = center)
            }
        }) {
            drawImage(
                image = sprite,
                srcOffset = srcOffset,
                srcSize = srcSize,
                dstOffset = IntOffset.Zero,
                dstSize = dstSize,
                filterQuality = FilterQuality.None // Сохраняет Pixel Art четким
            )
        }
    }
}
//@Composable
//fun Huntsman(
//    spriteSheetPath: String,
//    frameCount: Int,
//    framesPerRow: Int,
//    frameWidth: Int,
//    frameHeight: Int,
//    durationMillis: Int,
//    isLeft: Boolean = false,
//    loop: Boolean = true, // <-- НОВЫЙ ПАРАМЕТР: зацикливать ли анимацию?
//    onAnimationEnd: () -> Unit = {}, // <-- НОВЫЙ КОЛЛБЭК: что делать по окончании?
//    modifier: Modifier = Modifier
//) {
//    var byteArrayState by remember { mutableStateOf<ByteArray?>(null) }
//
//    // Загрузка байтов
//    LaunchedEffect(spriteSheetPath) {
//        byteArrayState = Res.readBytes(spriteSheetPath)
//    }
//
//    val sprite = remember(byteArrayState) {
//        byteArrayState?.decodeToImageBitmap()
//    }
//
//    // Создаем управляемый счетчик кадров
//    val frameAnimatable = remember { Animatable(0f) }
//
//    // Управляем анимацией вручную в зависимости от параметров loop и duration
//    LaunchedEffect(spriteSheetPath, frameCount, loop) {
//        // Каждый раз при смене анимации сбрасываем счетчик в ноль
//        frameAnimatable.snapTo(0f)
//
//        if (loop) {
//            // Бесконечный цикл для обычных анимаций (Idle, Walk)
//            frameAnimatable.animateTo(
//                targetValue = (frameCount - 1).toFloat(),
//                animationSpec = infiniteRepeatable(
//                    animation = tween(durationMillis = durationMillis, easing = LinearEasing),
//                    repeatMode = RepeatMode.Restart
//                )
//            )
//        } else {
//            // Одиночный запуск для спец-анимаций (Attack)
//            frameAnimatable.animateTo(
//                targetValue = (frameCount - 1).toFloat(),
//                animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
//            )
//            // Как только анимация дошла до конца, вызываем коллбэк
//            onAnimationEnd()
//        }
//    }
//
//    // Текущий кадр приводим к Integer
//    val currentFrame = frameAnimatable.value.toInt()
//
//    Canvas(modifier = modifier) {
//        sprite?.let { loadedSprite ->
//            // Ограничиваем индекс кадра для безопасности
//            val frame = currentFrame.coerceInBox(0, frameCount - 1)
//            val row = frame / framesPerRow
//            val col = frame % framesPerRow
//
//            val srcOffset = IntOffset(x = col * frameWidth, y = row * frameHeight)
//            val srcSize = IntSize(frameWidth, frameHeight)
//            val dstSize = IntSize(size.width.toInt(), size.height.toInt())
//
//            if (isLeft) {
//                withTransform({
//                    scale(scaleX = -1f, scaleY = 1f, pivot = center)
//                }) {
//                    drawImage(
//                        image = loadedSprite,
//                        srcOffset = srcOffset,
//                        srcSize = srcSize,
//                        dstOffset = IntOffset.Zero,
//                        dstSize = dstSize,
//                        filterQuality = FilterQuality.None
//                    )
//                }
//            } else {
//                drawImage(
//                    image = loadedSprite,
//                    srcOffset = srcOffset,
//                    srcSize = srcSize,
//                    dstOffset = IntOffset.Zero,
//                    dstSize = dstSize,
//                    filterQuality = FilterQuality.None
//                )
//            }
//        }
//    }
//}
//
enum class HuntsmanState(
    val drawable: DrawableResource,
    val frameCount: Int,
    val framesPerRow: Int,
    val durationMillis: Int,
    val loop: Boolean
) {
    IDLE(Res.drawable.huntsmanIdle, frameCount = 25, framesPerRow = 5, durationMillis = 500, loop = true),
    WALK(Res.drawable.huntsmanWalk, frameCount = 25, framesPerRow = 5, durationMillis = 1000, loop = true),
    ATTACK_RIFLE(Res.drawable.huntsmanAttackRifle, frameCount = 25, framesPerRow = 5, durationMillis = 1000, loop = false)
}