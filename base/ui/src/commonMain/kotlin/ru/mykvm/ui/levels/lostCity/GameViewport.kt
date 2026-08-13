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
import org.jetbrains.compose.resources.imageResource
import ru.mykvm.ui.hero.HeroState
import ru.mykvm.ui.model.CharacterAnimation
import ru.mykvm.ui.model.TileType

@Composable
fun GameViewport(
    cameraOffsetX: Float,       // Переданный интерполированный X активного героя
    activeHero: HeroState,      // Ссылка на текущего управляемого героя
    team: List<HeroState>,      // Список всей тройки для их одновременной отрисовки
    currentFrame: Int,          // Текущий кадр активного героя из TestMap
    gameTimeMillis: Long, // игровой таймер
    textures: LevelTextures,
    repeatableBrush: Brush,
    modifier: Modifier = Modifier
) {
    // Получаем предзагруженные ImageBitmap для каждого класса из общего конфига
    // (Рекомендуется кэшировать их в LevelTextures, чтобы не дергать imageResource каждый кадр)
    val heroBitmaps = team.associate { hero ->
        val config = hero.currentConfig
        hero.heroClass to imageResource(config.drawable)
    }

    Canvas(modifier = modifier) {
        val screenCenterX = size.width / 2f

        // 🔥 ИСПРАВЛЕНИЕ: Считаем сдвиг камеры напрямую от cameraOffsetX без лишних прибавок!
        val rawCameraOffsetX = screenCenterX - (cameraOffsetX + TILE_DISPLAY_SIZE / 2f)

        val mapTotalWidth = MAP_COLUMNS * TILE_DISPLAY_SIZE
        val minCameraX = size.width - mapTotalWidth

        // Ограничение камеры у краев уровня
        val finalCameraX = if (mapTotalWidth > size.width) rawCameraOffsetX.coerceIn(minCameraX, 0f) else 0f
        val groundY = size.height - TILE_DISPLAY_SIZE
        // ========================================================
        // 1. СЛОЙ 1: ДАЛЬНИЙ ПЛАН (НЕБО) - Скорость 5%
        // ========================================================
        val skyScrollX = finalCameraX * 0.05f
        withTransform({ translate(left = skyScrollX, top = 0f) }) {
            drawImage(
                image = textures.sky,
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(width = mapTotalWidth.toInt(), height = size.height.toInt()),
                filterQuality = FilterQuality.None
            )
        }

        // ========================================================
        // 2. СЛОЙ 2: СРЕДНИЙ ПЛАН (ГОРЫ / ЛЕС) - Скорость 25%
        // ========================================================
        val mountainScrollX = finalCameraX * 0.25f
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
        withTransform({ translate(left = finalCameraX, top = 0f) }) {
            for (col in 0 until MAP_COLUMNS) {
                val tile = gameMap[0][col]
                val tileTopLeft = Offset(col * TILE_DISPLAY_SIZE, groundY)
                val currentTileSize = IntSize(TILE_DISPLAY_SIZE.toInt(), TILE_DISPLAY_SIZE.toInt())

                when (tile) {
                    TileType.GRASS -> drawImage(image = textures.grass, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_START -> drawImage(image = textures.wallStart, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_END -> drawImage(image = textures.wallEnd, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.EMPTY -> {
                        drawImage(image = textures.empty, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    }
                }
            }
        }

        // ========================================================
        // 4. СЛОЙ 4: ОТРЕСОВКА ВСЕЙ КОМАНДЫ (3 ПЕРСОНАЖА)
        // ========================================================
        team.forEach { hero ->
            val config = hero.currentConfig
            val currentBitmap = heroBitmaps[hero.heroClass] ?: return@forEach

            // Динамический расчет кадра для каждого героя
            val frame = if (hero === activeHero) {
                // Активный персонаж берет идеально синхронизированный кадр из TestMap
                currentFrame.coerceIn(0, config.frameCount - 1)
            } else {
                // Неактивные персонажи крутят анимацию IDLE по кругу, используя общее время игры
                val elapsed = gameTimeMillis.coerceAtLeast(0L)
                val frameDuration = (config.durationMillis / config.frameCount).coerceAtLeast(1)
                ((elapsed / frameDuration) % config.frameCount).toInt()
            }

            val col = frame % config.framesPerRow
            val row = frame / config.framesPerRow

            val srcOffset = IntOffset(x = col * 256, y = row * 256)
            val srcSize = IntSize(width = 256, height = 256)
            val dstSize = IntSize(width = TILE_DISPLAY_SIZE.toInt(), height = TILE_DISPLAY_SIZE.toInt())

            // Рассчитываем позицию X в игровом мире
            val worldX = if (hero === activeHero) {
                cameraOffsetX // Активный плавно перемещается (включая прыжок) через animateOffsetAsState
            } else {
                hero.playerX * TILE_DISPLAY_SIZE // Неактивные стоят на своих фиксированных плитках
            }

            // Боевой выпад атаки (срабатывает, только если этот конкретный герой нажал ATK)
            val attackVisualOffset = if (hero.currentAnim == CharacterAnimation.ATTACK) {
                val offsetProgress = if (frame <= 12) frame / 12f else (24 - frame) / 12f
                val maxOffsetDistance = TILE_DISPLAY_SIZE * 0.5f
                val direction = if (hero.isLeft) -1f else 1f
                maxOffsetDistance * offsetProgress * direction
            } else {
                0f
            }

            // Итоговые экранные координаты с учетом сдвига камеры и высоты прыжка
            val playerDrawX = worldX + finalCameraX + attackVisualOffset
            val playerDrawY = groundY + hero.jumpOffsetY.value

            withTransform({
                translate(left = playerDrawX, top = playerDrawY)
                if (hero.isLeft) {
                    // Отражаем спрайт по горизонтали, если персонаж смотрит влево
                    scale(scaleX = -1f, scaleY = 1f, pivot = Offset(TILE_DISPLAY_SIZE / 2f, TILE_DISPLAY_SIZE / 2f))
                }
            }) {
                drawImage(
                    image = currentBitmap,
                    srcOffset = srcOffset,
                    srcSize = srcSize,
                    dstOffset = IntOffset.Zero,
                    dstSize = dstSize,
                    filterQuality = FilterQuality.None // Сохраняет пиксель-арт четким
                )
            }
        }
    }
}