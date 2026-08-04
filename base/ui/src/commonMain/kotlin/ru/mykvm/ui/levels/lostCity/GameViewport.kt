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

        // Ограничение камеры у краев уровня
        val cameraOffsetX = if (mapTotalWidth > size.width) rawCameraOffsetX.coerceIn(minCameraX, 0f) else 0f
        val groundY = size.height - TILE_DISPLAY_SIZE

        // ========================================================
        // СЕКРЕТНЫЙ БЛОК: Прогрев видеопамяти
        // ========================================================
        val isFirstFrame = currentFrame == 0 && currentState == HuntsmanState.IDLE
        if (isFirstFrame) {
            drawImage(image = textures.playerWalk, dstSize = IntSize(1, 1), dstOffset = IntOffset.Zero)
            drawImage(image = textures.playerAttack, dstSize = IntSize(1, 1), dstOffset = IntOffset.Zero)
        }

        // ========================================================
        // 1. СЛОЙ 1: ДАЛЬНИЙ ПЛАН (НЕБО) - Скорость 5%
        // ========================================================
        val skyScrollX = cameraOffsetX * 0.05f
        withTransform({ translate(left = skyScrollX, top = 0f) }) {
            drawImage(
                image = textures.sky,
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(width = (MAP_COLUMNS * TILE_DISPLAY_SIZE).toInt(), height = size.height.toInt()),
                filterQuality = FilterQuality.None
            )
        }

        // ========================================================
        // 2. СЛОЙ 2: СРЕДНИЙ ПЛАН (ГОРЫ / ЛЕС) - Скорость 25%
        // ========================================================
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

        // ========================================================
        // 3. СЛОЙ 3: ИГРОВАЯ КАРТА - Скорость 100%
        // ========================================================
        withTransform({ translate(left = cameraOffsetX, top = 0f) }) {
            for (col in 0 until MAP_COLUMNS) {
                // ИСПРАВЛЕНИЕ: Возвращаем [0][col], чтобы читать конкретный тайл из двумерного массива
                val tile = gameMap[0][col]
                val tileTopLeft = Offset(col * TILE_DISPLAY_SIZE, groundY)
                val currentTileSize = IntSize(TILE_DISPLAY_SIZE.toInt(), TILE_DISPLAY_SIZE.toInt())

                when (tile) {
                    TileType.GRASS -> drawImage(image = textures.grass, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_START -> drawImage(image = textures.wallStart, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_END -> drawImage(image = textures.wallEnd, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.EMPTY -> {
                        // Пустота — сквозь ямы видно задний план параллакса
//                        drawImage(image = textures.empty, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    }
                }
            }
        }

        // ========================================================
        // 4. СЛОЙ 4: ПЕРСОНАЖ
        // ========================================================
        val frame = currentFrame.coerceIn(0, currentState.frameCount - 1)
        val col = frame % currentState.framesPerRow
        val row = frame / currentState.framesPerRow

        val srcOffset = IntOffset(x = col * 256, y = row * 256)
        val srcSize = IntSize(width = 256, height = 256)
        val dstSize = IntSize(width = TILE_DISPLAY_SIZE.toInt(), height = TILE_DISPLAY_SIZE.toInt())

        val currentBitmap = when(currentState) {
            HuntsmanState.IDLE -> textures.playerIdle
            HuntsmanState.WALK -> textures.playerWalk
            HuntsmanState.ATTACK_RIFLE -> textures.playerAttack
        }

        // ========================================================
        // ИСПРАВЛЕНИЕ АТАКИ: Чистый и надежный боевой выпад на основе кадров
        // ========================================================
        val attackVisualOffset = if (currentState == HuntsmanState.ATTACK_RIFLE) {
            // У вас 25 кадров. С 0 по 12 кадр персонаж делает шаг вперед, с 13 по 24 — плавно возвращается назад
            val offsetProgress = if (frame <= 12) {
                frame / 12f // идет вперед от 0.0 до 1.0
            } else {
                (24 - frame) / 12f // идет назад от 1.0 до 0.0
            }

            // Сдвигаем визуально на половину нового крупного тайла (так выпад смотрится реалистично)
            val maxOffsetDistance = TILE_DISPLAY_SIZE * 0.5f
            val direction = if (isLeft) -1f else 1f

            maxOffsetDistance * offsetProgress * direction
        } else {
            0f
        }

        // Собираем итоговую координату (без дерганья камеры)
        val playerDrawX = animatedOffsetX + cameraOffsetX + attackVisualOffset
        val playerDrawY = groundY + jumpOffsetY

        withTransform({
            translate(left = playerDrawX, top = playerDrawY)
            if (isLeft) {
                scale(scaleX = -1f, scaleY = 1f, pivot = Offset(TILE_DISPLAY_SIZE / 2f, TILE_DISPLAY_SIZE / 2f))
            }
        }) {
            drawImage(
                image = currentBitmap,
                srcOffset = srcOffset,
                srcSize = srcSize,
                dstOffset = IntOffset.Zero,
                dstSize = dstSize,
                filterQuality = FilterQuality.None
            )
        }
    }
}