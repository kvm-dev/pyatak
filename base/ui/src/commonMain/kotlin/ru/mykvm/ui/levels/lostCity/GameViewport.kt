package ru.mykvm.ui.levels.lostCity

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.imageResource
import androidx.compose.ui.graphics.FilterQuality
import ru.mykvm.ui.enemy.EnemyState
import ru.mykvm.ui.hero.HeroState
import ru.mykvm.ui.model.CharacterAnimation
import ru.mykvm.ui.model.EnemyAnimation
import ru.mykvm.ui.model.TileType
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.drawText

@Composable
fun GameViewport(
    deletedTiles: Set<Pair<Int, Int>>,
    cameraOffsetX: Float,
    activeHero: HeroState,
    team: List<HeroState>,
    enemies: List<EnemyState>,
    currentFrame: Int,
    gameTimeMillis: Long,
    textures: LevelTextures,
    repeatableBrush: Brush,
    modifier: Modifier = Modifier
) {
    // Текстовый измеритель для рисования имен и здоровья над головами врагов
    val textMeasurer = rememberTextMeasurer()

    // Предзагрузка битмапов для классов героев
    val heroBitmaps = team.associate { hero ->
        val config = hero.currentConfig
        hero.heroType to imageResource(config.drawable)
    }

    // Рассчитываем плавные анимации позиций для каждого врага в контексте @Composable
    val animatedEnemyPositions = enemies.associateWith { enemy ->
        animateFloatAsState(
            targetValue = enemy.x * TILE_DISPLAY_SIZE,
            animationSpec = tween(durationMillis = 300, easing = LinearEasing),
            label = "EnemyX_${enemy.enemyType.displayName}"
        )
    }

    Canvas(modifier = modifier) {
        val screenCenterX = size.width / 2f

        // Считаем сдвиг камеры напрямую от cameraOffsetX
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
        // 3. СЛОЙ 3: ИГРОВАЯ КАРТА (ДВУХСЛОЙНЫЙ ТАКТИЧЕСКИЙ РЕНДЕР)
        // ========================================================
        withTransform({ translate(left = finalCameraX, top = 0f) }) {
            for (col in 0 until MAP_COLUMNS) {
                // 🔥 ИСПРАВЛЕНИЕ: Берем конкретную плитку по строке Y и колонке X
                val tile = gameMap.getOrNull(activeHero.playerY)?.getOrNull(col) ?: TileType.EMPTY

                val tileTopLeft = Offset(col * TILE_DISPLAY_SIZE, groundY)
                val currentTileSize = IntSize(TILE_DISPLAY_SIZE.toInt(), TILE_DISPLAY_SIZE.toInt())

                // Проверяем, не истощен ли данный тайл
                val isDepleted = deletedTiles.contains(Pair(col, activeHero.playerY))

                when (tile) {
                    // Стандартные базовые тайлы уровня
                    TileType.GRASS -> drawImage(image = textures.surface, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_START -> drawImage(image = textures.wallStart, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.WALL_END -> drawImage(image = textures.wallEnd, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)
                    TileType.EMPTY -> drawImage(image = textures.empty, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)

                    // РЕСУРСНЫЕ ТАЙЛЫ: Сначала всегда рисуем обычную траву
                    TileType.MINER_TILE, TileType.HUNTSMAN_TILE, TileType.MECHANIC_TILE,
                    TileType.DOCTOR_TILE, TileType.HUMAN_TILE, TileType.SHAMAN_TILE -> {

                        // Слой 1: Основание земли (Трава)
                        drawImage(image = textures.surface, dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()), dstSize = currentTileSize, filterQuality = FilterQuality.None)

                        // Слой 2: Если жила еще полна, накатываем сверху слой ресурса
                        if (!isDepleted) {
                            val resourceOverlay = when (tile) {
                                TileType.MINER_TILE -> textures.minerTile
                                TileType.HUNTSMAN_TILE -> textures.huntsmanTile
                                TileType.MECHANIC_TILE -> textures.mechanicTile
                                TileType.DOCTOR_TILE -> textures.doctorTile
                                TileType.HUMAN_TILE -> textures.humanTile
                                TileType.SHAMAN_TILE -> textures.shamanTile
                                else -> textures.empty
                            }

                            drawImage(
                                image = resourceOverlay,
                                dstOffset = IntOffset(tileTopLeft.x.toInt(), tileTopLeft.y.toInt()),
                                dstSize = currentTileSize,
                                filterQuality = FilterQuality.None
                            )
                        }
                    }
                }
            }
        }

        // ========================================================
        // СЛОЙ 3.5: ОТРЕСОВКА ВРАГОВ И ИХ ИНДИКАТОРОВ ЖИЗНЕЙ
        // ========================================================
        enemies.forEach { enemy ->
            if (enemy.health <= 0) return@forEach // Мертвых полностью скрываем

            val config = enemy.currentConfig
            val enemyAnims = textures.enemyBitmaps[enemy.enemyType] ?: return@forEach
            val currentBitmap = enemyAnims[enemy.currentAnim] ?: enemyAnims[EnemyAnimation.IDLE] ?: return@forEach

            // Расчет текущего кадра анимации врага
            val elapsed = gameTimeMillis - enemy.animationStartTime
            val frameDuration = (config.durationMillis / config.frameCount).coerceAtLeast(1)
            val frame = if (config.loop) {
                ((elapsed / frameDuration) % config.frameCount).toInt()
            } else {
                (elapsed / frameDuration).toInt().coerceIn(0, config.frameCount - 1)
            }

            val col = frame % config.framesPerRow
            val row = frame / config.framesPerRow

            val srcOffset = IntOffset(x = col * 256, y = row * 256)
            val srcSize = IntSize(256, 256)
            val dstSize = IntSize(TILE_DISPLAY_SIZE.toInt(), TILE_DISPLAY_SIZE.toInt())

            // Достаем плавную интерполированную координату врага
            val animatedX = animatedEnemyPositions[enemy]?.value ?: (enemy.x * TILE_DISPLAY_SIZE)
            val enemyDrawX = animatedX + finalCameraX
            val enemyDrawY = groundY + enemy.jumpOffsetY

            // 1. Отрисовка спрайта врага
            withTransform({
                translate(left = enemyDrawX, top = enemyDrawY)
                if (enemy.isLeft) {
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

            // 2. Отрисовка текста (Название и Сердечко с цифрой жизней) над головой врага
            val hudText = "${enemy.enemyType.displayName.uppercase()}  ❤️ ${enemy.health}"

            val isBoss = enemy.enemyType.displayName.contains("Босс")
            val textStyle = TextStyle(
                color = if (isBoss) Color(0xFFFF3D00) else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            // Измеряем текст для вычисления его точных габаритов
            val textLayoutResult = textMeasurer.measure(text = hudText, style = textStyle)
            val textWidth = textLayoutResult.size.width.toFloat()

            // Центрируем горизонтально над спрайтом и приподнимаем на 25 пикселей вверх
            val textX = enemyDrawX + (TILE_DISPLAY_SIZE / 2f) - (textWidth / 2f)
            val textY = enemyDrawY - 25f
            val isTextFullyVisible = textX >= 0f && (textX + textWidth) <= size.width
            if (isTextFullyVisible) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = hudText,
                    style = textStyle,
                    topLeft = Offset(textX, textY)
                )
            }
        }
// ========================================================
// 4. СЛОЙ 4: ОТРЕСОВКА ВСЕЙ КОМАНДЫ (3 ПЕРСОНАЖА)
// ========================================================
        team.forEach { hero ->
            if (hero.health <= 0) return@forEach // Погибших героев полностью скрываем с карты

            val config = hero.currentConfig
            val currentBitmap = heroBitmaps[hero.heroType] ?: return@forEach

            // Динамический расчет кадра для каждого героя
            val frame = if (hero === activeHero) {
                currentFrame.coerceIn(0, config.frameCount - 1)
            } else {
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
                cameraOffsetX
            } else {
                hero.playerX * TILE_DISPLAY_SIZE
            }

            // Боевой выпад атаки (срабатывает, только если этот конкретного герой нажал ATK)
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

            // Отрисовка самого спрайта персонажа (Без HUD текста над головой)
            withTransform({
                translate(left = playerDrawX, top = playerDrawY)
                if (hero.isLeft) {
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
}