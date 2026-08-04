package ru.mykvm.ui.levels.lostCity

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
import ru.mykvm.ui.model.TileType

@Composable
fun GameViewport(
    animatedOffsetX: Float,
    jumpOffsetY: Float,
    currentFrame: Int,
    currentState: HuntsmanState,
    isLeft: Boolean,
    textures: LevelTextures,
    repeatableBrush: Brush,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val screenCenterX = size.width / 2f
        val rawCameraOffsetX = screenCenterX - (animatedOffsetX + TILE_DISPLAY_SIZE / 2f)
        val mapTotalWidth = MAP_COLUMNS * TILE_DISPLAY_SIZE
        val minCameraX = size.width - mapTotalWidth

        val cameraOffsetX = if (mapTotalWidth > size.width) rawCameraOffsetX.coerceIn(minCameraX, 0f) else 0f
        val groundY = size.height - TILE_DISPLAY_SIZE

        // 1. Небо (Параллакс)
        val skyScrollX = cameraOffsetX * 0.05f
        withTransform({ translate(left = skyScrollX, top = 0f) }) {
            drawImage(
                image = textures.sky,
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(width = (MAP_COLUMNS * TILE_DISPLAY_SIZE).toInt(), height = size.height.toInt()),
                filterQuality = FilterQuality.None
            )
        }

        // 2. Лес (Параллакс)
        val mountainScrollX = cameraOffsetX * 0.25f
        withTransform({ translate(left = mountainScrollX, top = 0f) }) {
            val leftBuffer = 1000f
            withTransform({ translate(left = -leftBuffer, top = groundY - TILE_DISPLAY_SIZE * 1.2f) }) {
                drawRect(
                    brush = repeatableBrush,
                    size = Size(mapTotalWidth + leftBuffer * 2, TILE_DISPLAY_SIZE * 3f)
                )
            }
        }

        // 3. Игровая Карта
        withTransform({ translate(left = cameraOffsetX, top = 0f) }) {
            for (col in 0 until MAP_COLUMNS) {
                // ИСПРАВЛЕНИЕ: Добавлен индекс строки playerY (0), как было в вашем коде
                val tile = gameMap[0][col]
                val tileTopLeft = Offset(col * TILE_DISPLAY_SIZE, groundY)
                val currentTileSize = IntSize(TILE_DISPLAY_SIZE.toInt(), TILE_DISPLAY_SIZE.toInt())

                when (tile) {
                    TileType.GRASS -> drawImage(image = textures.grass, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_START -> drawImage(image = textures.wallStart, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_END -> drawImage(image = textures.wallEnd, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.EMPTY -> drawImage(image = textures.empty, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                }
            }
        }

        // 4. Персонаж
        val frame = currentFrame.coerceIn(0, currentState.frameCount - 1)
        val col = frame % currentState.framesPerRow
        val row = frame / currentState.framesPerRow

        val srcOffset = IntOffset(x = col * 256, y = row * 256)
        val srcSize = IntSize(width = 256, height = 256)
        val dstSize = IntSize(width = TILE_DISPLAY_SIZE.toInt(), height = TILE_DISPLAY_SIZE.toInt())

        val playerDrawX = animatedOffsetX + cameraOffsetX
        val playerDrawY = groundY + jumpOffsetY

        withTransform({
            translate(left = playerDrawX, top = playerDrawY)
            if (isLeft) {
                scale(scaleX = -1f, scaleY = 1f, pivot = Offset(TILE_DISPLAY_SIZE / 2f, TILE_DISPLAY_SIZE / 2f))
            }
        }) {
            drawImage(
                image = textures.playerSprite,
                srcOffset = srcOffset,
                srcSize = srcSize,
                dstOffset = IntOffset.Zero,
                dstSize = dstSize,
                filterQuality = FilterQuality.None
            )
        }
    }
}