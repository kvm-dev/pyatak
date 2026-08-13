package ru.mykvm.ui.levels

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mykvm.ui.control.BottomGamePad
import ru.mykvm.ui.control.TopHeroSelector
import ru.mykvm.ui.hero.HeroClass
import ru.mykvm.ui.hero.HeroState
import ru.mykvm.ui.levels.lostCity.GameViewport
import ru.mykvm.ui.levels.lostCity.LevelTextures
import ru.mykvm.ui.levels.lostCity.MAP_COLUMNS
import ru.mykvm.ui.levels.lostCity.TILE_DISPLAY_SIZE
import ru.mykvm.ui.levels.lostCity.gameMap
import ru.mykvm.ui.model.CharacterAnimation
import ru.mykvm.ui.model.TileType
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun TestMap(initialTeamClasses: List<HeroClass>) {
    require(initialTeamClasses.size == 3) { "В команде должно быть ровно 3 персонажа!" }

    val coroutineScope = rememberCoroutineScope()

    // 1. Инициализируем состояние всей тройки (спавним их, например, на соседних клетках: 1, 2, 3)
    val team = remember {
        initialTeamClasses.mapIndexed { index, heroClass ->
            HeroState(heroClass = heroClass, startX = index + 1)
        }
    }

    // 2. Индекс персонажа, которым мы управляем в данный момент
    var activeHeroIndex by remember { mutableStateOf(0) }
    val activeHero = team[activeHeroIndex]

    // Флаги команд для активного героя
    var isLeftPressed by remember { mutableStateOf(false) }
    var isRightPressed by remember { mutableStateOf(false) }

    // Текстуры
    val textures = LevelTextures.rememberTextures()
    val repeatableBrush = remember(textures.mountains) {
        ShaderBrush(ImageShader(image = textures.mountains, tileModeX = TileMode.Repeated, tileModeY = TileMode.Clamp))
    }

    // Стабильный игровой таймер кадров (один на всю игру)
    var gameTimeMillis by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) { withFrameMillis { _ -> gameTimeMillis += 16L } }
    }

    // Железобетонный таймер стамины для ВСЕХ персонажей в команде
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1.seconds) // Проверка каждую секунду

            team.forEach { hero ->
                // Условие покоя стало намного проще и надежнее:
                // Персонаж отдыхает, если он не летит в воздухе, не падает в бездну и игрок не держит кнопки шага
                val isActuallyResting = hero.jumpOffsetY.value == 0f && !hero.isFalling && !isLeftPressed && !isRightPressed

                if (isActuallyResting && hero.stamina < 10) {
                    hero.stamina += 1

                    // Если стамина восстановилась хотя бы до 1, а персонаж завис в анимации WALK,
                    // мягко возвращаем его в IDLE, чтобы вернуть управление
                    if (hero.stamina == 1 && hero.currentAnim == CharacterAnimation.WALK && !hero.isMoving) {
                        hero.currentAnim = CharacterAnimation.IDLE
                    }
                }
            }
        }
    }

    LaunchedEffect(activeHero.stamina) {
        if (activeHero.stamina <= 0) {
            isLeftPressed = false
            isRightPressed = false

            // Дополнительно: если персонаж застрял в анимации ходьбы без движения, возвращаем в IDLE
            if (activeHero.currentAnim == CharacterAnimation.WALK && !activeHero.isMoving) {
                activeHero.currentAnim = CharacterAnimation.IDLE
            }
        }
    }

    // контроль падения
    LaunchedEffect(activeHero.playerX, activeHero.jumpOffsetY.value, activeHero.isFalling) {
        // КРИТИЧЕСКАЯ ЗАЩИТА: Если герой уже падает или находится в воздухе (высота < 0) — полностью игнорируем пустоту под ним!
        if (activeHero.isFalling || activeHero.jumpOffsetY.value < 0f) return@LaunchedEffect

        // Физическая проверка срабатывает СТРОГО при приземлении (когда jumpOffsetY вернется ровно в 0f)
        if (gameMap[activeHero.playerY][activeHero.playerX] == TileType.EMPTY) {
            activeHero.isFalling = true
            activeHero.isMoving = false
            isLeftPressed = false
            isRightPressed = false
            activeHero.currentAnim = CharacterAnimation.IDLE

            activeHero.health = (activeHero.health - 1).coerceAtLeast(0)

            coroutineScope.launch {
                activeHero.jumpOffsetY.animateTo(TILE_DISPLAY_SIZE * 1.5f, tween(250, easing = FastOutLinearInEasing))
                if (activeHero.health <= 0) { activeHero.health = 5; activeHero.stamina = 10 }
                activeHero.playerX = 1
                activeHero.jumpOffsetY.snapTo(0f)
                activeHero.isFalling = false
            }
        }
    }

    // Сброс атаки активного героя
    LaunchedEffect(activeHero.currentAnim) {
        if (activeHero.currentAnim == CharacterAnimation.ATTACK) {
            kotlinx.coroutines.delay(activeHero.currentConfig.durationMillis.toLong().milliseconds)
            isLeftPressed = false
            isRightPressed = false
            activeHero.currentAnim = CharacterAnimation.IDLE
        }
    }

    // Расчет текущего кадра для АКТИВНОГО персонажа
    val currentFrame = remember(activeHero.currentAnim, gameTimeMillis) {
        val config = activeHero.currentConfig
        val elapsed = (gameTimeMillis - activeHero.animationStartTime).coerceAtLeast(0L)
        val frameDuration = (config.durationMillis / config.frameCount).coerceAtLeast(1)

        if (config.loop) {
            ((elapsed / frameDuration) % config.frameCount).toInt()
        } else {
            (elapsed / frameDuration).toInt().coerceIn(0, config.frameCount - 1)
        }
    }

    // Игровой цикл перемещения АКТИВНОГО персонажа
    LaunchedEffect(isLeftPressed, isRightPressed, activeHero.isMoving, activeHero.stamina, activeHero.currentAnim) {
        // 1. Если ни одна кнопка не нажата — выходим
        if (!isLeftPressed && !isRightPressed) return@LaunchedEffect

        // 2. Запоминаем направление, в которое хотел пойти игрок
        val dX = when {
            isLeftPressed -> -1
            isRightPressed -> 1
            else -> null
        }

        // 3. КРИТИЧЕСКОЕ ИСПРАВЛЕНИЕ: Мгновенно гасим флаги одиночного тапа!
        // Теперь они гарантированно сбросятся в false, даже если у героя 0 стамины
        // или он занят другой анимацией.
        isLeftPressed = false
        isRightPressed = false

        // 4. Только ТЕПЕРЬ проверяем, может ли персонаж физически сделать шаг
        if (activeHero.isMoving || activeHero.isFalling || activeHero.stamina <= 0 || activeHero.currentAnim == CharacterAnimation.ATTACK) return@LaunchedEffect

        if (dX != null) {
            val newX = activeHero.playerX + dX
            activeHero.isLeft = dX < 0

            if (newX in 0 until MAP_COLUMNS) {
                val targetTile = gameMap[activeHero.playerY][newX]
                if (targetTile.isWalkable || targetTile == TileType.EMPTY) {
                    activeHero.stamina -= 1
                    activeHero.moveDuration = 300
                    activeHero.currentAnim = CharacterAnimation.WALK
                    activeHero.playerX = newX
                    activeHero.isMoving = true // Запуск анимации animateOffsetAsState
                    activeHero.animationStartTime = gameTimeMillis
                }
            }
        }
    }

    // Плавная интерполяция перемещения для АКТИВНОГО героя
    // (Для неактивных героев можно сделать аналогичные animateOffset,
    // но главное — камера будет следовать за ТЕКУЩИМ анимированным X)
    val cameraOffsetX by animateOffsetAsState(
        targetValue = Offset(activeHero.playerX * TILE_DISPLAY_SIZE, 0f),
        // Использует moveDuration, который равен 300мс для шага и 350мс для прыжка
        animationSpec = tween(durationMillis = activeHero.moveDuration, easing = LinearEasing),
        finishedListener = {
            // Сбрасываем флаг движения, как только персонаж физически прибыл в целевую плитку
            activeHero.isMoving = false

            // Возвращаем персонажа в состояние покоя, если он не занят атакой
            if (activeHero.currentAnim != CharacterAnimation.ATTACK) {
                activeHero.currentAnim = CharacterAnimation.IDLE
                activeHero.animationStartTime = gameTimeMillis // Сбрасываем таймер для плавной анимации покоя
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))
    ) {
        // 1. ИГРОВОЙ ЭКРАН (Canvas) — теперь занимает центральную/нижнюю часть экрана свободно
        GameViewport(
            cameraOffsetX = cameraOffsetX.x,
            activeHero = activeHero,
            team = team,
            currentFrame = currentFrame,
            gameTimeMillis = gameTimeMillis,
            textures = textures,
            repeatableBrush = repeatableBrush,
            modifier = Modifier
                .fillMaxWidth()
                .height((TILE_DISPLAY_SIZE * 3.5f).dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 180.dp) // Чуть приподняли, чтобы нижний геймпад его не перекрывал
        )
        val isInputAllowed = activeHero.currentAnim == CharacterAnimation.IDLE
        // 2. ВЕРХНЯЯ ПАНЕЛЬ: Аватарки персонажей для переключения
        TopHeroSelector(
            team = team,
            activeHeroIndex = activeHeroIndex,
            onHeroSelected = { index ->
                // Переключить можно, только если текущий герой абсолютно свободен (Input Allowed)
                if (index != activeHeroIndex && isInputAllowed) {
                    isLeftPressed = false
                    isRightPressed = false
                    activeHeroIndex = index
                    team[index].animationStartTime = gameTimeMillis
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        )

        // 3. НИЖНЯЯ ПАНЕЛЬ: Геймпад и статус текущего героя
        val controlsEnabled = !activeHero.isFalling && activeHero.stamina > 0

                && !activeHero.isMoving
                && activeHero.jumpOffsetY.value == 0f
                && !activeHero.isFalling
        BottomGamePad(
            activeHero = activeHero,
            // Теперь кнопки заблокируются, если персонаж делает ХОТЬ ЧТО-ТО
            controlsEnabled = isInputAllowed && activeHero.stamina > 0,
            staminaAvailable = activeHero.stamina > 0,
            onLeftClick = { isLeftPressed = true },
            onRightClick = { isRightPressed = true },
            onAttackClick = {
                // Дополнительная проверка прямо в лямбде для безопасности
                if (isInputAllowed && activeHero.stamina > 0) {
                    activeHero.stamina -= 1
                    activeHero.currentAnim = CharacterAnimation.ATTACK
                    activeHero.animationStartTime = gameTimeMillis
                }
            },
            onJumpClick = {
                if (isInputAllowed && activeHero.stamina > 0) {
                    val jumpDirection = if (activeHero.isLeft) -2 else 2
                    val targetX = activeHero.playerX + jumpDirection
                    val nextX = activeHero.playerX + (if (activeHero.isLeft) -1 else 1)

                    val isInsideMap = targetX in 0 until MAP_COLUMNS && nextX in 0 until MAP_COLUMNS
                    val currentTile = gameMap[activeHero.playerY][activeHero.playerX]
                    val isStandingOnSafeGround = currentTile != TileType.EMPTY

                    val canJump = isInsideMap && isStandingOnSafeGround &&
                            gameMap[activeHero.playerY][targetX] != TileType.WALL_START &&
                            gameMap[activeHero.playerY][nextX] != TileType.WALL_START &&
                            gameMap[activeHero.playerY][targetX] != TileType.WALL_END &&
                            gameMap[activeHero.playerY][nextX] != TileType.WALL_END

                    if (canJump) {
                        activeHero.stamina -= 1

                        // Задаем время полета по горизонтали
                        activeHero.moveDuration = 350

                        // Включаем анимацию прыжка
                        activeHero.currentAnim = CharacterAnimation.JUMP
                        activeHero.animationStartTime = gameTimeMillis

                        // Мгновенно меняем координату цели — камера плавно поедет сама через animateOffsetAsState!
                        activeHero.playerX = targetX
                        activeHero.isMoving = true

                        // Корутина занимается ТОЛЬКО вертикальной осью Y
                        coroutineScope.launch {
                            // Взлет вверх (175 мс)
                            activeHero.jumpOffsetY.animateTo(-250f, tween(175, easing = FastOutSlowInEasing))
                            // Падение на землю (175 мс)
                            activeHero.jumpOffsetY.animateTo(0f, tween(175, easing = LinearOutSlowInEasing))
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        )
    }
}