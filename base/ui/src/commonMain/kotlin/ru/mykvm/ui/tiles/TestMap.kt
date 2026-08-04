package ru.mykvm.ui.tiles

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import ru.mykvm.ui.creatures.hauntsman.Huntsman
import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
//import ru.mykvm.ui.creatures.hauntsman.HuntsmanState
import ru.mykvm.ui.model.TileType
import kotlin.coroutines.ContinuationInterceptor

val gameMap: List<List<TileType>> = listOf(
    // 2 - левая стена (граница)
    // 1 - безопасная трава
    // 0 - пустота (пропасти, которые нужно перепрыгивать)
    listOf(2, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 2)
).map { row ->
    row.map { id ->
        when (id) {
            1 -> TileType.GRASS
            2 -> TileType.WALL
            else -> TileType.EMPTY
        }
    }
}
// Настройки игрового мира
const val MAP_COLUMNS = 14
const val MAP_ROWS = 1
const val TILE_DISPLAY_SIZE = 220f

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

    // Флаг, блокирующий управление во время падения в яму
    var isFalling by remember { mutableStateOf(false) }

    var moveDuration by remember { mutableStateOf(180) }

    val spriteBitmap = imageResource(currentState.drawable)

    val frameAnimatable = remember { Animatable(0f) }
    LaunchedEffect(currentState, isMoving) {
        frameAnimatable.snapTo(0f)
        if (currentState.loop) {
            frameAnimatable.animateTo(
                targetValue = (currentState.frameCount - 1).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = currentState.durationMillis,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            frameAnimatable.animateTo(
                targetValue = (currentState.frameCount - 1).toFloat(),
                animationSpec = tween(
                    durationMillis = currentState.durationMillis,
                    easing = LinearEasing
                )
            )
            if (currentState == HuntsmanState.ATTACK_RIFLE) {
                currentState =
                    if (isLeftPressed || isRightPressed) HuntsmanState.WALK else HuntsmanState.IDLE
            }
        }
    }

    // Игровой цикл перемещения персонажа (С поддержкой падения в яму)
    LaunchedEffect(isLeftPressed, isRightPressed, isMoving, isJumping, isFalling, currentState) {
        // Если прыгаем, падаем в яму, двигаемся или атакуем — управление заблокировано
        if (isMoving || isJumping || isFalling || currentState == HuntsmanState.ATTACK_RIFLE) return@LaunchedEffect

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

                // ИСПРАВЛЕНИЕ: Теперь разрешаем шаг, если впереди ТРАВА ИЛИ ЯМА!
                if (targetTile.isWalkable || targetTile == TileType.EMPTY) {
                    moveDuration = 180
                    currentState = HuntsmanState.WALK
                    playerX = newX
                    isMoving = true
                } else {
                    // Уперлись в стену (WALL) — стоим в IDLE
                    currentState = HuntsmanState.IDLE
                }
            } else {
                currentState = HuntsmanState.IDLE
            }
        }
    }

    // Плавная интерполяция перемещения персонажа и камеры по оси X
    val animatedOffsetX by animateOffsetAsState(
        targetValue = Offset(playerX * TILE_DISPLAY_SIZE, 0f),
        animationSpec = tween(durationMillis = moveDuration, easing = LinearEasing),
        finishedListener = {
            isMoving = false

            // ИСПРАВЛЕНИЕ: Логика честного падения вниз при обычном шаге в яму
            if (jumpOffsetY.value == 0f && gameMap[playerY][playerX] == TileType.EMPTY) {
                isFalling = true
                currentState = HuntsmanState.IDLE // Или стейт боли/падения, если есть
                coroutineScope.launch {
                    // Плавно утягиваем персонажа вниз под текстуры (на 150 пикселей вниз)
                    jumpOffsetY.animateTo(150f, tween(300, easing = FastOutLinearInEasing))

                    // Респавн на безопасную траву
                    playerX = 1
                    jumpOffsetY.snapTo(0f) // Мгновенно возвращаем высоту в норму
                    isFalling = false
                    isLeftPressed = false
                    isRightPressed = false
                }
                return@animateOffsetAsState
            }

            // Логика сброса состояний после прыжка или атаки
            if (moveDuration == 500 || currentState == HuntsmanState.ATTACK_RIFLE) {
                isLeftPressed = false
                isRightPressed = false
                currentState = HuntsmanState.IDLE
            } else {
                if (!isLeftPressed && !isRightPressed && jumpOffsetY.value == 0f) {
                    currentState = HuntsmanState.IDLE
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // --- 1. ИГРОВОЙ ЭКРАН (Опущен вниз) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((TILE_DISPLAY_SIZE * 3.5f).dp)
                // ИСПРАВЛЕНИЕ: Выравниваем игровое поле по нижнему краю экрана
                .align(Alignment.BottomCenter)
                // Оставляем отступ снизу в 160.dp, чтобы поле стояло ровно НАД кнопками управления
                .padding(bottom = 160.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val screenCenterX = size.width / 2f
                val cameraOffsetX = screenCenterX - (animatedOffsetX.x + TILE_DISPLAY_SIZE / 2f)

                // Вся отрисовка карты смещается к самому низу Canvas (size.height - TILE_DISPLAY_SIZE)
                val groundY = size.height - TILE_DISPLAY_SIZE

                withTransform({
                    translate(left = cameraOffsetX, top = 0f)
                }) {
                    for (col in 0 until MAP_COLUMNS) {
                        val tile = gameMap[playerY][col]
                        drawRect(
                            color = Color(tile.colorHex),
                            // ИСПРАВЛЕНИЕ: Тайлы теперь рисуются на линии земли Canvas
                            topLeft = Offset(col * TILE_DISPLAY_SIZE, groundY),
                            size = Size(TILE_DISPLAY_SIZE, TILE_DISPLAY_SIZE)
                        )
                    }
                }

                val frame = frameAnimatable.value.toInt().coerceIn(0, currentState.frameCount - 1)
                val col = frame % currentState.framesPerRow
                val row = frame / currentState.framesPerRow

                val srcOffset = IntOffset(x = col * 256, y = row * 256)
                val srcSize = IntSize(width = 256, height = 256)
                val dstSize = IntSize(width = TILE_DISPLAY_SIZE.toInt(), height = TILE_DISPLAY_SIZE.toInt())

                val playerDrawX = screenCenterX - TILE_DISPLAY_SIZE / 2f
                // ИСПРАВЛЕНИЕ: Персонаж стоит на линии земли (groundY) и прыгает/падает относительно нее
                val playerDrawY = groundY + jumpOffsetY.value

                withTransform({
                    translate(left = playerDrawX, top = playerDrawY)
                    if (isLeft) {
                        scale(
                            scaleX = -1f,
                            scaleY = 1f,
                            pivot = Offset(TILE_DISPLAY_SIZE / 2f, TILE_DISPLAY_SIZE / 2f)
                        )
                    }
                }) {
                    drawImage(
                        image = spriteBitmap,
                        srcOffset = srcOffset,
                        srcSize = srcSize,
                        dstOffset = IntOffset.Zero,
                        dstSize = dstSize,
                        filterQuality = FilterQuality.None
                    )
                }
            }
        }

        // --- 2. МОБИЛЬНЫЙ ИНТЕРФЕЙС УПРАВЛЕНИЯ (Зафиксирован в самом низу) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .zIndex(5f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                val controlsEnabled = !isFalling
                DirectionButton(text = "◀", onPressChanged = { isPressed -> if (controlsEnabled) isLeftPressed = isPressed })
                DirectionButton(text = "▶", onPressChanged = { isPressed -> if (controlsEnabled) isRightPressed = isPressed })
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                // КНОПКА ПРЫЖКА
                Surface(
                    onClick = {
                        if (jumpOffsetY.value == 0f && !isFalling && currentState != HuntsmanState.ATTACK_RIFLE) {
                            val jumpDirection = if (isLeft) -2 else 2
                            val targetX = playerX + jumpDirection

                            val nextX = playerX + (if (isLeft) -1 else 1)
                            val canJump = targetX in 0 until MAP_COLUMNS &&
                                    gameMap[playerY][targetX] != TileType.WALL &&
                                    gameMap[playerY][nextX] != TileType.WALL

                            if (canJump) {
                                moveDuration = 500
                                playerX = targetX
                                isMoving = true
                                if (currentState != HuntsmanState.WALK) currentState = HuntsmanState.WALK
                            }

                            coroutineScope.launch {
                                jumpOffsetY.animateTo(-250f, tween(280, easing = FastOutSlowInEasing))
                                jumpOffsetY.animateTo(0f, tween(220, easing = LinearOutSlowInEasing))

                                if (gameMap[playerY][playerX] == TileType.EMPTY) {
                                    isFalling = true
                                    // Падение вниз под новую линию земли
                                    jumpOffsetY.animateTo(200f, tween(300, easing = FastOutLinearInEasing))
                                    playerX = 1
                                    jumpOffsetY.snapTo(0f)
                                    isFalling = false
                                }
                            }
                        }
                    },
                    shape = CircleShape,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "▲", color = Color.White, fontSize = 22.sp)
                    }
                }

                // КНОПКА АТАКИ
                Surface(
                    onClick = {
                        if (!isMoving && !isFalling && jumpOffsetY.value == 0f && currentState != HuntsmanState.ATTACK_RIFLE) {
                            val attackDirection = if (isLeft) -1 else 1
                            val targetX = playerX + attackDirection

                            val canMoveAndAttack = targetX in 0 until MAP_COLUMNS &&
                                    gameMap[playerY][targetX].isWalkable

                            if (canMoveAndAttack) {
                                moveDuration = HuntsmanState.ATTACK_RIFLE.durationMillis
                                playerX = targetX
                                isMoving = true
                            }
                            currentState = HuntsmanState.ATTACK_RIFLE
                        }
                    },
                    shape = CircleShape,
                    color = Color(0xFFC62828),
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "ATK", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun DirectionButton(
    text: String,
    onPressChanged: (Boolean) -> Unit
) {
    Surface(
        shape = CircleShape,
        color = Color(0xFF424242),
        modifier = Modifier
            .size(60.dp)
            // Прямой перехват системных тач-событий экрана мобильного
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Press -> {
                                onPressChanged(true) // Палец на кнопке -> Бежим!
                            }
                            PointerEventType.Release -> {
                                onPressChanged(false) // Палец убрали -> Стоп!
                            }
                        }
                    }
                }
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = Color.White, fontSize = 22.sp)
        }
    }
}