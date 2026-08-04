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
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
import ru.mykvm.ui.control.GameHUDControls
import ru.mykvm.ui.levels.lostCity.GameViewport
import ru.mykvm.ui.levels.lostCity.LevelTextures
import ru.mykvm.ui.levels.lostCity.MAP_COLUMNS
import ru.mykvm.ui.levels.lostCity.TILE_DISPLAY_SIZE
import ru.mykvm.ui.levels.lostCity.gameMap
//import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
import ru.mykvm.ui.model.TileType
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TestMap() {
    val coroutineScope = rememberCoroutineScope()

    var isLevelReady by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500.milliseconds) // Даем игре 500мс на спокойную загрузку и центрирование
        isLevelReady = true
    }

    var playerX by remember { mutableStateOf(1) }
    val playerY = 0

    var currentState by remember { mutableStateOf(HuntsmanState.IDLE) }
    var isLeft by remember { mutableStateOf(false) }

    var isLeftPressed by remember { mutableStateOf(false) }
    var isRightPressed by remember { mutableStateOf(false) }

    var isMoving by remember { mutableStateOf(false) }
    val jumpOffsetY = remember { Animatable(0f) }
    val isJumping by remember { derivedStateOf { jumpOffsetY.value < 0f } }
    var isFalling by remember { mutableStateOf(false) }

    // Стартовая скорость по умолчанию совпадает со скоростью шага
    var moveDuration by remember { mutableStateOf(300) }

    var health by remember { mutableStateOf(5) }
    var stamina by remember { mutableStateOf(10) }

    LaunchedEffect(Unit) {
        while (true) {
            // Проверяем каждую секунду
            kotlinx.coroutines.delay(1000.milliseconds)

            // Если персонаж никуда не бежит, не прыгает и не атакует
            val isResting = currentState == HuntsmanState.IDLE && !isMoving && !isJumping && !isFalling

            if (isResting && stamina < 10) {
                stamina += 1 // Медленно восстанавливаем по 1 единице
            }
        }
    }

    // Прогреваем и кэшируем все текстуры сразу при старте игры
    val textures = LevelTextures.rememberTextures()
    val repeatableBrush = remember(textures.mountains) {
        ShaderBrush(
            ImageShader(
                image = textures.mountains,
                tileModeX = TileMode.Repeated,
                tileModeY = TileMode.Clamp
            )
        )
    }

    // Игровой таймер времени для покадровой анимации (withFrameMillis)
    var gameTimeMillis by remember { mutableStateOf(0L) }

    // Вместо сырого системного времени делаем стабильный инкремент игрового времени
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                // Каждый кадр экрана (при 60fps это ~16.6мс) прибавляем стабильный шаг
                gameTimeMillis += 16L
            }
        }
    }

    // Фиксируем время начала текущей анимации в рамках НАШЕГО игрового времени
    var animationStartTime by remember { mutableStateOf(0L) }
    LaunchedEffect(currentState) {
        animationStartTime = gameTimeMillis
    }

    // Рассчитываем кадр персонажа (Теперь время идет строго от 0мс без скачков)
    val currentFrame = remember(currentState, gameTimeMillis) {
        val totalFrames = currentState.frameCount
        val duration = currentState.durationMillis
        val elapsed = (gameTimeMillis - animationStartTime).coerceAtLeast(0L)
        val frameDuration = (duration / totalFrames).coerceAtLeast(1)

        if (currentState.loop) {
            ((elapsed / frameDuration) % totalFrames).toInt()
        } else {
            // Для атаки: просто считаем кадр, не переключая стейты внутри лямбды
            val frame = (elapsed / frameDuration).toInt()
            frame.coerceIn(0, totalFrames - 1)
        }
    }

    LaunchedEffect(currentState) {
        if (currentState == HuntsmanState.ATTACK_RIFLE) {
            // Ждем ровно столько, сколько длится анимация атаки (1000 мс)
            kotlinx.coroutines.delay(currentState.durationMillis.toLong().milliseconds)

            // Гарантированно возвращаем в IDLE или WALK и сбрасываем кнопки
            isLeftPressed = false
            isRightPressed = false
            currentState = HuntsmanState.IDLE
        }
    }

    // Игровой цикл перемещения персонажа (Обычный одиночный шаг)
    LaunchedEffect(
        isLeftPressed,
        isRightPressed,
        isMoving,
        isJumping,
        isFalling,
        currentState,
        stamina
    ) {
        if (currentState == HuntsmanState.ATTACK_RIFLE || isMoving || isJumping || isFalling || stamina <= 0) return@LaunchedEffect

        val dX = when {
            isLeftPressed -> -1
            isRightPressed -> 1
            else -> null
        }

        if (dX != null) {
            val newX = playerX + dX
            isLeft = dX < 0

            if (newX in 0 until MAP_COLUMNS) {
                val targetTile = gameMap[playerY][newX]
                if (targetTile.isWalkable || targetTile == TileType.EMPTY) {
                    stamina -= 1
                    moveDuration = 300
                    if (currentState != HuntsmanState.WALK) currentState = HuntsmanState.WALK
                    playerX = newX
                    isMoving = true
                }
                isLeftPressed = false
                isRightPressed = false
            }
        }
    }

    // Плавная интерполяция перемещения персонажа и камеры по оси X
    val animatedOffsetX by animateOffsetAsState(
        targetValue = Offset(playerX * TILE_DISPLAY_SIZE, 0f),
        animationSpec = tween(durationMillis = moveDuration, easing = LinearEasing),
        finishedListener = {
            isMoving = false

            // Логика падения в бездну
            if (jumpOffsetY.value == 0f && gameMap[playerY][playerX] == TileType.EMPTY) {
                isFalling = true
                currentState = HuntsmanState.IDLE
                health = (health - 1).coerceAtLeast(0)

                coroutineScope.launch {
                    jumpOffsetY.animateTo(
                        TILE_DISPLAY_SIZE * 1.5f,
                        tween(250, easing = FastOutLinearInEasing)
                    )
                    if (health <= 0) {
                        health = 5; stamina = 10
                    }
                    playerX = 1
                    jumpOffsetY.snapTo(0f)
                    isFalling = false
                    isLeftPressed = false
                    isRightPressed = false
                }
                return@animateOffsetAsState
            }

            // Безусловный сброс в IDLE после окончания любого перемещения
            isLeftPressed = false
            isRightPressed = false
            if (currentState != HuntsmanState.ATTACK_RIFLE) {
                currentState = HuntsmanState.IDLE
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))
    ) {
        // Отрисовка игрового экрана (Вынесенный Canvas)
        GameViewport(
            animatedOffsetX = animatedOffsetX.x,
            jumpOffsetY = jumpOffsetY.value,
            currentFrame = currentFrame,
            currentState = currentState,
            isLeft = isLeft,
            textures = textures,
            repeatableBrush = repeatableBrush,
            modifier = Modifier
                .fillMaxWidth()
                .height((TILE_DISPLAY_SIZE * 3.5f).dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 160.dp)
        )

        // Панель геймпада со счетчиками (Вынесенный HUD)
        val controlsEnabled = isLevelReady && !isFalling && stamina > 0
        GameHUDControls(
            health = health,
            stamina = stamina,
            controlsEnabled = controlsEnabled,
            staminaAvailable = stamina > 0,
            onLeftClick = { isLeftPressed = true },
            onRightClick = { isRightPressed = true },
            onAttackClick = {
                // Атака доступна, если есть стамина, персонаж на земле и не находится в процессе шага/прыжка
                if (stamina > 0 && !isMoving && jumpOffsetY.value == 0f && currentState != HuntsmanState.ATTACK_RIFLE) {
                    stamina -= 1
                    currentState = HuntsmanState.ATTACK_RIFLE // Canvas сам сделает визуальный выпад вперед-назад
                }
            },
            onJumpClick = {
                if (jumpOffsetY.value == 0f && controlsEnabled && currentState != HuntsmanState.ATTACK_RIFLE) {
                    val jumpDirection = if (isLeft) -2 else 2
                    val targetX = playerX + jumpDirection
                    val nextX = playerX + (if (isLeft) -1 else 1)

                    val canJump = targetX in 0 until MAP_COLUMNS &&
                            gameMap[playerY][targetX] != TileType.WALL_START &&
                            gameMap[playerY][nextX] != TileType.WALL_START &&
                            gameMap[playerY][targetX] != TileType.WALL_END &&
                            gameMap[playerY][nextX] != TileType.WALL_END

                    if (canJump) {
                        stamina -= 1
                        moveDuration = 350 // Скорость быстрого прыжка
                        playerX = targetX
                        isMoving = true
                        currentState = HuntsmanState.WALK
                    }

                    coroutineScope.launch {
                        jumpOffsetY.animateTo(-250f, tween(200, easing = FastOutSlowInEasing))
                        jumpOffsetY.animateTo(0f, tween(150, easing = LinearOutSlowInEasing))

                        if (gameMap[playerY][playerX] == TileType.EMPTY) {
                            isFalling = true
                            health = (health - 1).coerceAtLeast(0)
                            jumpOffsetY.animateTo(
                                TILE_DISPLAY_SIZE * 1.5f,
                                tween(250, easing = FastOutLinearInEasing)
                            )
                            if (health <= 0) {
                                health = 5; stamina = 10
                            }
                            playerX = 1
                            jumpOffsetY.snapTo(0f)
                            isFalling = false
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .zIndex(5f)
        )
    }
}