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
    var moveDuration by remember { mutableStateOf(180) }

    var health by remember { mutableStateOf(5) }
    var stamina by remember { mutableStateOf(10) }

    // Использование вынесенных структур данных и текстур
    val textures = LevelTextures.rememberTextures(currentState)
    val repeatableBrush = remember(textures.mountains) {
        ShaderBrush(ImageShader(image = textures.mountains, tileModeX = TileMode.Repeated, tileModeY = TileMode.Clamp))
    }

    var gameTimeMillis by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) { withFrameMillis { time -> gameTimeMillis = time } }
    }

    var animationStartTime by remember { mutableStateOf(0L) }
    LaunchedEffect(currentState) { animationStartTime = gameTimeMillis }

    LaunchedEffect(currentState, stamina) {
        if (currentState == HuntsmanState.IDLE && stamina < 10) {
            kotlinx.coroutines.delay(10000.milliseconds)
            stamina = (stamina + 1).coerceAtMost(10)
        }
    }

    val currentFrame = remember(currentState, gameTimeMillis) {
        val totalFrames = currentState.frameCount
        val duration = currentState.durationMillis
        val elapsed = (gameTimeMillis - animationStartTime).coerceAtLeast(0L)
        val frameDuration = (duration / totalFrames).coerceAtLeast(1)

        if (currentState.loop) {
            ((elapsed / frameDuration) % totalFrames).toInt()
        } else {
            val frame = (elapsed / frameDuration).toInt()
            if (frame >= totalFrames) {
                coroutineScope.launch {
                    if (currentState == HuntsmanState.ATTACK_RIFLE) {
                        currentState = if (isLeftPressed || isRightPressed) HuntsmanState.WALK else HuntsmanState.IDLE
                    }
                }
                totalFrames - 1
            } else {
                frame
            }
        }
    }

    // Обработка одиночного шага движения
    LaunchedEffect(isLeftPressed, isRightPressed, isMoving, isJumping, isFalling, currentState, stamina) {
        if (isMoving || isJumping || isFalling || currentState == HuntsmanState.ATTACK_RIFLE) return@LaunchedEffect

        val dX = when {
            isLeftPressed -> -1
            isRightPressed -> 1
            else -> null
        }

        if (dX != null) {
            // ИСПРАВЛЕНИЕ: Если стамины нет, сбрасываем нажатие и не даем идти,
            // но как только стамина восстановится, LaunchedEffect сработает заново на следующий клик!
            if (stamina <= 0) {
                isLeftPressed = false
                isRightPressed = false
                return@LaunchedEffect
            }

            val newX = playerX + dX
            isLeft = dX < 0

            if (newX in 0 until MAP_COLUMNS) {
                val targetTile = gameMap[playerY][newX]
                if (targetTile.isWalkable || targetTile == TileType.EMPTY) {
                    stamina -= 1 // Расходуем выносливость
                    moveDuration = 300
                    if (currentState != HuntsmanState.WALK) currentState = HuntsmanState.WALK
                    playerX = newX
                    isMoving = true

                    isLeftPressed = false
                    isRightPressed = false
                } else {
                    if (currentState != HuntsmanState.IDLE) currentState = HuntsmanState.IDLE
                    isLeftPressed = false
                    isRightPressed = false
                }
            } else {
                if (currentState != HuntsmanState.IDLE) currentState = HuntsmanState.IDLE
                isLeftPressed = false
                isRightPressed = false
            }
        }
    }

    val animatedOffsetX by animateOffsetAsState(
        targetValue = Offset(playerX * TILE_DISPLAY_SIZE, 0f),
        animationSpec = tween(durationMillis = moveDuration, easing = LinearEasing),
        finishedListener = {
            isMoving = false

            // 1. Если приземлились или зашли в пропасть — плавно падаем вниз и респавнимся
            if (jumpOffsetY.value == 0f && gameMap[playerY][playerX] == TileType.EMPTY) {
                isFalling = true
                currentState = HuntsmanState.IDLE
                health = (health - 1).coerceAtLeast(0)

                coroutineScope.launch {
                    jumpOffsetY.animateTo(TILE_DISPLAY_SIZE * 1.5f, tween(250, easing = FastOutLinearInEasing))
                    if (health <= 0) {
                        health = 5
                        stamina = 10
                    }
                    playerX = 1
                    jumpOffsetY.snapTo(0f)
                    isFalling = false
                    isLeftPressed = false
                    isRightPressed = false
                }
                return@animateOffsetAsState
            }

            // 2. ИСПРАВЛЕНИЕ: Безусловный сброс в IDLE для любого типа перемещения!
            // Раз у нас пошаговое управление (без зажатия), то как только шаг/прыжок/выпад закончился —
            // мы ГАРАНТИРОВАННО возвращаем персонажа в стойку покоя, если он сейчас не атакует.
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
        // Вызов вынесенного Canvas-экрана
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

        // Вызов вынесенного геймпада управления
        val controlsEnabled = !isFalling && stamina > 0
        GameHUDControls(
            health = health,
            stamina = stamina,
            controlsEnabled = !isFalling && stamina > 0,
            staminaAvailable = stamina > 0,
            // ИСПРАВЛЕНИЕ: Простая и надежная установка флагов старта движения
            onLeftClick = { isLeftPressed = true },
            onRightClick = { isRightPressed = true },
            onAttackClick = {
                stamina -= 1
                val attackDirection = if (isLeft) -1 else 1
                val targetX = playerX + attackDirection
                if (targetX in 0 until MAP_COLUMNS && gameMap[playerY][targetX].isWalkable) {
                    moveDuration = HuntsmanState.ATTACK_RIFLE.durationMillis
                    playerX = targetX
                    isMoving = true
                }
                currentState = HuntsmanState.ATTACK_RIFLE
            },
            onJumpClick = {
                if (jumpOffsetY.value == 0f && currentState != HuntsmanState.ATTACK_RIFLE) {
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
                        moveDuration = 350 // Время быстрого прыжка
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
                            jumpOffsetY.animateTo(TILE_DISPLAY_SIZE * 1.5f, tween(250, easing = FastOutLinearInEasing))
                            if (health <= 0) { health = 5; stamina = 10 }
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