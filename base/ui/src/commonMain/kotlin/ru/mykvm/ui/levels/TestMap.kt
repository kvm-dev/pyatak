package ru.mykvm.ui.levels

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import org.koin.compose.koinInject
import ru.mykvm.ui.control.BottomGamePad
import ru.mykvm.ui.control.TopHeroSelector
import ru.mykvm.ui.enemy.EnemyState
import ru.mykvm.ui.enemy.EnemyType
import ru.mykvm.ui.hero.HeroType
import ru.mykvm.ui.hero.HeroState
import ru.mykvm.ui.levels.lostCity.GameViewport
import ru.mykvm.ui.levels.lostCity.LevelTextures
import ru.mykvm.ui.levels.lostCity.MAP_COLUMNS
import ru.mykvm.ui.levels.lostCity.TILE_DISPLAY_SIZE
import ru.mykvm.ui.levels.lostCity.gameMap
import ru.mykvm.ui.model.CharacterAnimation
import ru.mykvm.ui.model.EnemyAnimation
import ru.mykvm.ui.model.GameSound
import ru.mykvm.ui.model.ResourceType
import ru.mykvm.ui.model.TileType
import ru.mykvm.ui.utils.SoundPlayer
import kotlin.time.Duration.Companion.seconds

@Composable
fun TestMap(initialTeamClasses: List<HeroType>, onGameOver: () -> Unit, onLevelComplete: (resources: Map<ResourceType, Int>, deadHeroes: List<HeroType>) -> Unit) {
    require(initialTeamClasses.size == 3) { "В команде должно быть ровно 3 персонажа!" }
    val soundPlayer: SoundPlayer = koinInject()
    val coroutineScope = rememberCoroutineScope()
    val gatheredResources = remember { mutableStateMapOf<ResourceType, Int>() }
    val depletedTiles = remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
    // 1. Инициализируем состояние всей тройки (спавним их, например, на соседних клетках: 1, 2, 3)
    val team = remember {
        initialTeamClasses.mapIndexed { index, heroClass ->
            HeroState(heroType = heroClass, startX = index + 1)
        }
    }

    val enemies = remember {
        listOf(
            EnemyState(EnemyType.PATROL, startX = 6),
            EnemyState(EnemyType.ROBOT, startX = 12),
            EnemyState(EnemyType.BOSS_KNIGHT, startX = 17)
        )
    }

    // 2. Индекс персонажа, которым мы управляем в данный момент
    var activeHeroIndex by remember { mutableStateOf(0) }
    val activeHero = team[activeHeroIndex]


    // Флаги команд для активного героя
    var isLeftPressed by remember { mutableStateOf(false) }
    var isRightPressed by remember { mutableStateOf(false) }

    // Текстуры
    val textures = LevelTextures.rememberTextures(
        enemiesInLevel = listOf(
            EnemyType.PATROL,
            EnemyType.ROBOT, EnemyType.BOSS_KNIGHT
        )
    )
    val repeatableBrush = remember(textures.levelScene) {
        ShaderBrush(
            ImageShader(
                image = textures.levelScene,
                tileModeX = TileMode.Repeated,
                tileModeY = TileMode.Clamp
            )
        )
    }

    // Стабильный игровой таймер кадров (один на всю игру)
    var gameTimeMillis by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { _ -> gameTimeMillis += 16L }
        }
    }

    // Железобетонный таймер стамины для ВСЕХ персонажей в команде
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(3.seconds) // Проверка каждую секунду

            team.forEach { hero ->
                // Условие покоя стало намного проще и надежнее:
                // Персонаж отдыхает, если он не летит в воздухе, не падает в бездну и игрок не держит кнопки шага
                val isActuallyResting =
                    hero.jumpOffsetY.value == 0f && !hero.isFalling && !isLeftPressed && !isRightPressed

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

    fun switchToNextAliveHero() {
        val nextAliveIndex = team.indexOfFirst { it.health > 0 }
        if (nextAliveIndex != -1) {
            activeHeroIndex = nextAliveIndex
            team[nextAliveIndex].animationStartTime = gameTimeMillis
        } else {
            // Если живых героев в массиве больше нет — вызываем внешнюю функцию конца игры
            onGameOver()
        }
    }

// ЖЕЛЕЗОБЕТОННЫЙ ИСПРАВЛЕННЫЙ ИИ-КОНТРОЛЛЕР В TESTMAP
    var lastAiTickTime by remember { mutableStateOf(0L) }

    LaunchedEffect(gameTimeMillis) {
        // Каждые 1500 мс (1.5 секунды) запускаем логику ИИ
        if (gameTimeMillis - lastAiTickTime >= 1500L) {
            lastAiTickTime = gameTimeMillis

            if (activeHero.isFalling) return@LaunchedEffect

            enemies.forEach { enemy ->
                if (enemy.health <= 0) return@forEach

                // 1. Пропускаем такт, если враг в ступоре/щите
                if (enemy.aiStunDurationMillis > 0) {
                    enemy.aiStunDurationMillis =
                        (enemy.aiStunDurationMillis - 1500L).coerceAtLeast(0L)
                    if (enemy.aiStunDurationMillis == 0L) {
                        enemy.currentAnim = EnemyAnimation.IDLE
                        enemy.animationStartTime = gameTimeMillis
                    }
                    return@forEach
                }

                // 2. Сброс не зацикленных анимаций
                val currentElapsed = gameTimeMillis - enemy.animationStartTime
                if (!enemy.currentConfig.loop && currentElapsed >= enemy.currentConfig.durationMillis) {
                    enemy.currentAnim = EnemyAnimation.IDLE
                    enemy.animationStartTime = gameTimeMillis
                }

                // Расчеты дистанции и направления до игрока
                val distanceToPlayer = kotlin.math.abs(activeHero.playerX - enemy.x)
                val directionToPlayer = if (activeHero.playerX < enemy.x) -1 else 1

                // 🔥 ВЫЧИСЛЯЕМ МАКСИМАЛЬНЫЙ РАДИУС АТАКИ ВРАГА
                val maxEnemyAttackDistance = if (enemy.enemyType.isRangeAttack) 2 else 1

                // ========================================================
                // РЕЖИМ 1: АТАКА (Игрок находится в радиусе поражения оружия)
                // ========================================================
                if (distanceToPlayer <= maxEnemyAttackDistance) {
                    enemy.isLeft = directionToPlayer < 0

                    if (enemy.isBossSecondPhase && enemy.enemyType.animations.containsKey(
                            EnemyAnimation.SPECIAL_ATTACK
                        )
                    ) {
                        enemy.currentAnim = EnemyAnimation.SPECIAL_ATTACK
                        soundPlayer.playSound(GameSound.ENEMY_SPECIAL_ATTACK, enemyType = enemy.enemyType)
                    } else {
                        enemy.currentAnim = EnemyAnimation.ATTACK
                        soundPlayer.playSound(GameSound.ENEMY_ATTACK, enemyType = enemy.enemyType)
                    }

                    enemy.animationStartTime = gameTimeMillis

                    // Наносим урон герою, только если он твердо стоит на земле (не в прыжке)
                    if (activeHero.jumpOffsetY.value == 0f && !activeHero.isFalling) {
                        val damageToHero = if (enemy.isBossSecondPhase) 2 else 1
                        activeHero.health = (activeHero.health - damageToHero).coerceAtLeast(0)

                        // 🔥 ИСПРАВЛЕНИЕ: Возвращаем правильный триггер командной смерти персонажа
                        if (activeHero.health <= 0) {
                            isLeftPressed = false
                            isRightPressed = false
                            activeHero.currentAnim = CharacterAnimation.IDLE

                            // Вызываем функцию, которая переключит на следующего живого
                            // или завершит игру через onGameOver(), если мертвы все три!
                            switchToNextAliveHero()
                        }
                    }
                    return@forEach
                }

                // ========================================================
                // 🔥 НОВЫЙ РЕЖИМ: ПРОВЕРКА ПРЫЖКА ЧЕРЕЗ БЕЗДНУ (Платформер ИИ)
                // ========================================================
                val jumpDirection = directionToPlayer // Направление к игроку (-1 или 1)
                val nextTileX = enemy.x + jumpDirection
                val targetJumpX = enemy.x + (jumpDirection * 2)

                // Проверяем, умеет ли данный тип врага вообще прыгать (задан ли JUMP в enum)
                val canThisEnemyTypeJump =
                    enemy.enemyType.animations.containsKey(EnemyAnimation.JUMP)

                if (canThisEnemyTypeJump && distanceToPlayer >= 2) {
                    val nextTile = gameMap.getOrNull(0)?.getOrNull(nextTileX)
                    val targetTile = gameMap.getOrNull(0)?.getOrNull(targetJumpX)

                    // Условия: впереди бездна (EMPTY), а через клетку — твердая земля (isWalkable)
                    val isGapInFront = nextTile == TileType.EMPTY
                    val isLandingSafe = targetTile != null && targetTile.isWalkable

                    if (isGapInFront && isLandingSafe) {
                        // 🔥 ПРОВЕРКА: Занята ли целевая клетка targetJumpX кем-то из ЖИВЫХ героев команды
                        val isTileOccupiedByHero =
                            team.any { hero -> hero.playerX == targetJumpX && hero.health > 0 }

                        if (!isTileOccupiedByHero) {
                            // Все условия сошлись! Запускаем процесс прыжка
                            enemy.isLeft = jumpDirection < 0
                            enemy.currentAnim = EnemyAnimation.JUMP
                            soundPlayer.playSound(GameSound.ENEMY_JUMP)
                            enemy.animationStartTime = gameTimeMillis
                            enemy.x = targetJumpX // GameViewport плавно покатит его по X за 300мс

                            // Запускаем корутину для физики прыжка вверх-вниз по оси Y
                            coroutineScope.launch {
                                // Имитируем прыжок: плавно поднимаем на -200 пикселей и опускаем обратно в 0f
                                // Суммарно за 300мс, чтобы совпасть со скоростью шага по X
                                enemy.jumpOffsetY = -200f
                                kotlinx.coroutines.delay(150)
                                enemy.jumpOffsetY = 0f
                            }
                            return@forEach // Так завершен, враг прыгнул!
                        }
                    }
                }

                // ========================================================
                // РЕЖИМ 2: ПРЕСЛЕДОВАНИЕ/СБЛИЖЕНИЕ
                // ========================================================
                // Враг идет к игроку, только если тот находится ДАЛЬШЕ его радиуса атаки,
                // но в зоне видимости (например, до 4 клеток)
                if (distanceToPlayer in (maxEnemyAttackDistance + 1)..4) {
                    val nextX = enemy.x + directionToPlayer
                    val targetTile = gameMap.getOrNull(0)?.getOrNull(nextX)

                    if (targetTile != null && targetTile.isWalkable) {
                        enemy.isLeft = directionToPlayer < 0
                        enemy.currentAnim = EnemyAnimation.WALK
                        enemy.animationStartTime = gameTimeMillis
                        enemy.x = nextX
                    }
                    return@forEach
                }

                // ========================================================
                // РЕЖИМ 3: САМОСТОЯТЕЛЬНОЕ ПАТРУЛИРОВАНИЕ (Игрок слишком далеко)
                // ========================================================
                val randomChoice = (0..2).random()

                if (randomChoice == 0) {
                    if (enemy.currentAnim == EnemyAnimation.WALK) {
                        enemy.currentAnim = EnemyAnimation.IDLE
                        enemy.animationStartTime = gameTimeMillis
                    }
                } else {
                    val patrolDirection = if (randomChoice == 1) -1 else 1
                    val nextX = enemy.x + patrolDirection

                    val isInsidePatrolZone =
                        kotlin.math.abs(nextX - enemy.spawnX) <= enemy.patrolRadius
                    val targetTile = gameMap.getOrNull(0)?.getOrNull(nextX)

                    if (isInsidePatrolZone && targetTile != null && targetTile.isWalkable) {
                        enemy.isLeft = patrolDirection < 0
                        enemy.currentAnim = EnemyAnimation.WALK
                        enemy.animationStartTime = gameTimeMillis
                        enemy.x = nextX
                    } else {
                        enemy.isLeft = !enemy.isLeft
                        enemy.currentAnim = EnemyAnimation.IDLE
                        enemy.animationStartTime = gameTimeMillis
                    }
                }
            }
        }
    }

    // ЕДИНЫЙ КОНТРОЛЛЕР СБРОСА И СИНХРОНИЗАЦИИ АНИМАЦИЙ ВРАГОВ
    LaunchedEffect(gameTimeMillis) {
        enemies.forEach { enemy ->
            if (enemy.health <= 0) return@forEach

            // Если враг занят какой-то временной анимацией (не IDLE)
            if (enemy.currentAnim != EnemyAnimation.IDLE) {
                val config = enemy.currentConfig
                val elapsed = gameTimeMillis - enemy.animationStartTime

                // 1. Для ХОДЬБЫ (WALK) — гасим анимацию ног через 400 мс, чтобы они не скользили
                if (enemy.currentAnim == EnemyAnimation.WALK) {
                    if (elapsed >= 400L) {
                        enemy.currentAnim = EnemyAnimation.IDLE
                        enemy.animationStartTime = gameTimeMillis
                    }
                }
                // 2. Для ВСЕХ ОСТАЛЬНЫХ незацикленных анимаций (ATTACK, HURT, SPECIAL/Щит)
                // Возвращаем в IDLE строго тогда, когда спрайт-лист полностью проигрался по времени
                else if (!config.loop && elapsed >= config.durationMillis) {
                    enemy.currentAnim = EnemyAnimation.IDLE
                    enemy.animationStartTime = gameTimeMillis
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
        if (activeHero.isFalling || activeHero.jumpOffsetY.value < 0f) return@LaunchedEffect

        if (gameMap[activeHero.playerY][activeHero.playerX] == TileType.EMPTY) {
            activeHero.isFalling = true
            activeHero.isMoving = false
            isLeftPressed = false
            isRightPressed = false
            activeHero.currentAnim = CharacterAnimation.IDLE

            // Отнимаем жизнь за падение
            activeHero.health = (activeHero.health - 1).coerceAtLeast(0)

            coroutineScope.launch {
                activeHero.jumpOffsetY.animateTo(
                    TILE_DISPLAY_SIZE * 1.5f,
                    tween(250, easing = FastOutLinearInEasing)
                )

                // 🔥 НОВАЯ ЛОГИКА СМЕРТИ ОТ ПАДЕНИЯ
                if (activeHero.health <= 0) {
                    switchToNextAliveHero()
                } else {
                    // Если выжил — просто респавним на безопасную плитку
                    activeHero.playerX = 1
                }

                activeHero.jumpOffsetY.snapTo(0f)
                activeHero.isFalling = false
            }
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
    LaunchedEffect(
        isLeftPressed,
        isRightPressed,
        activeHero.isMoving,
        activeHero.stamina,
        activeHero.currentAnim
    ) {
        if (!isLeftPressed && !isRightPressed) return@LaunchedEffect

        val dX = when {
            isLeftPressed -> -1
            isRightPressed -> 1
            else -> null
        }

        isLeftPressed = false
        isRightPressed = false

        if (activeHero.isMoving || activeHero.isFalling || activeHero.stamina <= 0 || activeHero.currentAnim == CharacterAnimation.ATTACK) return@LaunchedEffect

        if (dX != null) {
            val newX = activeHero.playerX + dX
            activeHero.isLeft = dX < 0

            if (newX in 0 until MAP_COLUMNS) {
                val targetTile = gameMap[activeHero.playerY][newX]

                // 🔥 НОВАЯ ПРОВЕРКА: Ищем, стоит ли на целевой плитке newX живой враг
                val isTileOccupiedByEnemy = enemies.any { it.x == newX && it.health > 0 }

                // Герой делает шаг, только если тайл проходим (или бездна) И клетка НЕ занята врагом
                if ((targetTile.isWalkable || targetTile == TileType.EMPTY) && !isTileOccupiedByEnemy) {
                    activeHero.stamina -= 1
                    activeHero.moveDuration = 300
                    activeHero.currentAnim = CharacterAnimation.WALK
                    activeHero.playerX = newX
                    activeHero.isMoving = true
                    activeHero.animationStartTime = gameTimeMillis
                }
            }
        }
    }

    // Контроль завершения уровня при гибели ВСЕХ врагов
    LaunchedEffect(enemies.map { it.health }) {
        val allEnemiesDead = enemies.all { it.health <= 0 }
        if (allEnemiesDead && enemies.isNotEmpty()) {
            // Собираем список типов героев, у которых здоровье <= 0
            val deadHeroesList = team.filter { it.health <= 0 }.map { it.heroType }

            // Вызываем внешний метод завершения уровня
            onLevelComplete(gatheredResources.toMap(), deadHeroesList)
        }
    }

    // 🔥 ЕДИНЫЙ ЖЕЛЕЗОБЕТОННЫЙ КОНТРОЛЛЕР СБРОСА (Атака, Сбор, Отказ)
    LaunchedEffect(activeHero.currentAnim) {
        val current = activeHero.currentAnim

        if (current == CharacterAnimation.ATTACK ||
            current == CharacterAnimation.EXTRACT ||
            current == CharacterAnimation.REFUSE) {

            // Берем точную длительность проигранного спрайт-листа из конфигурации
            val duration = activeHero.currentConfig.durationMillis.toLong()
            kotlinx.coroutines.delay(duration)

            // Мгновенно возвращаем персонажа в строй и сбрасываем таймер для плавной анимации покоя
            activeHero.currentAnim = CharacterAnimation.IDLE
            activeHero.animationStartTime = gameTimeMillis
        }
    }

    // Плавная интерполяция перемещения для АКТИВНОГО героя
    // (Для неактивных героев можно сделать аналогичные animateOffset,
    // но главное — камера будет следовать за ТЕКУЩИМ анимированным X)
    val cameraOffsetX by animateOffsetAsState(
        targetValue = Offset(activeHero.playerX * TILE_DISPLAY_SIZE, 0f),
        animationSpec = tween(durationMillis = activeHero.moveDuration, easing = LinearEasing),
        finishedListener = {
            // Сбрасываем флаг движения, как только персонаж физически прибыл в целевую плитку
            activeHero.isMoving = false

            // 🔥 ИСПРАВЛЕНИЕ: Возвращаем в IDLE, только если герой НЕ занят атакой или сбором ресурсов!
            val isBusyWithSpecialAnim = activeHero.currentAnim == CharacterAnimation.ATTACK ||
                    activeHero.currentAnim == CharacterAnimation.EXTRACT ||
                    activeHero.currentAnim == CharacterAnimation.REFUSE

            if (!isBusyWithSpecialAnim) {
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
            deletedTiles = depletedTiles.value, // 🔥 НОВЫЙ ПАРАМЕТР
            modifier = Modifier
                .fillMaxWidth()
                .height((TILE_DISPLAY_SIZE * 3.5f).dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp),
            enemies = enemies
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

        // ========================================================
        // 3. НИЖНЯЯ ПАНЕЛЬ: Статус текущего героя и Геймпад
        // ========================================================

        // 🔥 НАДЕЖНЫЙ ОПРЕДЕЛИТЕЛЬ РЕСУРСНОГО ТАЙЛА
        val currentTile = gameMap.getOrNull(activeHero.playerY)?.getOrNull(activeHero.playerX) ?: TileType.EMPTY
        val isResourceTile = currentTile.requiredResource != null

        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))
        ) {
            // 1. ИГРОВОЙ ЭКРАН (Canvas)
            GameViewport(
                cameraOffsetX = cameraOffsetX.x,
                activeHero = activeHero,
                team = team,
                currentFrame = currentFrame,
                gameTimeMillis = gameTimeMillis,
                textures = textures,
                repeatableBrush = repeatableBrush,
                deletedTiles = depletedTiles.value, // 🔥 ВОТ ЭТА СТРОКА ИСПРАВИТ ОШИБКУ КОМПИЛЯЦИИ
                modifier = Modifier
                    .fillMaxWidth()
                    .height((TILE_DISPLAY_SIZE * 3.5f).dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 200.dp),
                enemies = enemies
            )

            val isInputAllowed = activeHero.currentAnim == CharacterAnimation.IDLE

            // 2. ВЕРХНЯЯ ПАНЕЛЬ: Аватарки персонажей
            TopHeroSelector(
                team = team,
                activeHeroIndex = activeHeroIndex,
                onHeroSelected = { index ->
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

            // 3. 🔥 НИЖНЯЯ ПАНЕЛЬ В РАМОЧКЕ: Всё сгруппировано в один красивый узел
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xEE121212), shape = RoundedCornerShape(24.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF444444),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                val rawTile = gameMap.getOrNull(activeHero.playerY)?.getOrNull(activeHero.playerX) ?: TileType.EMPTY
                val isTileDepleted = depletedTiles.value.contains(Pair(activeHero.playerX, activeHero.playerY))

                // 🔥 Если жила истощена, для логики это теперь обычная ТРАВА
                val currentTile = if (isTileDepleted) TileType.SURFACE else rawTile
                val isResourceTile = currentTile.requiredResource != null

                // Внутри вызова BottomGamePad обновите блок onExtractClick:
                BottomGamePad(
                    activeHero = activeHero,
                    controlsEnabled = (activeHero.currentAnim == CharacterAnimation.IDLE || activeHero.currentAnim == CharacterAnimation.WALK) && activeHero.health > 0 && activeHero.stamina > 0 && !activeHero.isFalling,
                    staminaAvailable = activeHero.stamina > 0,
                    isResourceTile = isResourceTile,
                    onLeftClick = { isLeftPressed = true },
                    onRightClick = { isRightPressed = true },
                    onAttackClick = {
                        if (isInputAllowed && activeHero.stamina > 0) {
                            activeHero.stamina -= 1
                            activeHero.currentAnim = CharacterAnimation.ATTACK
                            activeHero.animationStartTime = gameTimeMillis
                            soundPlayer.playSound(GameSound.ATTACK, activeHero.heroType)

                            val attackDirection = if (activeHero.isLeft) -1 else 1
                            val maxAttackDistance = if (activeHero.heroType.isRangeAttack) 2 else 1

                            val hitEnemy = enemies.find { enemy ->
                                val distanceX = (enemy.x - activeHero.playerX) * attackDirection
                                enemy.health > 0 && distanceX in 1..maxAttackDistance && enemy.currentAnim != EnemyAnimation.RESURRECT
                            }

                            if (hitEnemy != null) {
                                val isVulnerable = hitEnemy.enemyType.vulnerableTo == null ||
                                        hitEnemy.enemyType.vulnerableTo.contains(activeHero.heroType)

                                if (isVulnerable) {
                                    val damage = 1
                                    val newHealth = hitEnemy.health - damage
                                    soundPlayer.playSound(GameSound.HIT)
                                    val isFinalBoss = hitEnemy.enemyType == EnemyType.BOSS_LORD_HREWNDEL

                                    if (newHealth <= 0 && isFinalBoss && !hitEnemy.hasResurrected) {
                                        hitEnemy.health = hitEnemy.enemyType.maxHealth
                                        hitEnemy.hasResurrected = true
                                        hitEnemy.isBossSecondPhase = true
                                        hitEnemy.currentAnim = EnemyAnimation.RESURRECT
                                        hitEnemy.animationStartTime = gameTimeMillis

                                        // 🔥 Передаем enemyType, чтобы проиграть уникальный звук возрождения босса
                                        soundPlayer.playSound(GameSound.ENEMY_RESURRECT, enemyType = hitEnemy.enemyType)

                                        val resurrectConfig = hitEnemy.enemyType.animations[EnemyAnimation.RESURRECT]
                                        val duration = resurrectConfig?.durationMillis?.toLong() ?: 2000L
                                        hitEnemy.aiStunDurationMillis = duration
                                    } else {
                                        hitEnemy.health = newHealth.coerceAtLeast(0)
                                        if (hitEnemy.health > 0) {
                                            hitEnemy.currentAnim = EnemyAnimation.HURT
                                            hitEnemy.animationStartTime = gameTimeMillis
                                            hitEnemy.aiStunDurationMillis = 1000L
                                        }
                                    }
                                } else {
                                    // Игрок атаковал не тем персонажем -> Враг блокирует удар (Неуязвимость)
                                    hitEnemy.currentAnim = EnemyAnimation.SPECIAL
                                    hitEnemy.animationStartTime = gameTimeMillis
                                    hitEnemy.aiStunDurationMillis = 1500L

                                    // 🔥 ВСТАВЛЯЕМ СЮДА: Звук ухода в блок/активации защиты для конкретного моба!
                                    soundPlayer.playSound(GameSound.ENEMY_SPECIAL, enemyType = hitEnemy.enemyType)
                                }
                            }
                        }
                    },
                    onJumpClick = {
                        if (isInputAllowed && activeHero.stamina > 0) {
                            val jumpDirection = if (activeHero.isLeft) -2 else 2
                            val targetX = activeHero.playerX + jumpDirection
                            val nextX = activeHero.playerX + (if (activeHero.isLeft) -1 else 1)

                            val isInsideMap =
                                targetX in 0 until MAP_COLUMNS && nextX in 0 until MAP_COLUMNS
                            val currentHeroTile = gameMap[activeHero.playerY][activeHero.playerX]
                            val isStandingOnSafeGround = currentHeroTile != TileType.EMPTY

                            val isTargetTileFree = enemies.none { it.x == targetX && it.health > 0 }

                            val canJump = isInsideMap && isStandingOnSafeGround &&
                                    gameMap[activeHero.playerY][targetX] != TileType.WALL_START &&
                                    gameMap[activeHero.playerY][nextX] != TileType.WALL_START &&
                                    gameMap[activeHero.playerY][targetX] != TileType.WALL_END &&
                                    gameMap[activeHero.playerY][nextX] != TileType.WALL_END &&
                                    isTargetTileFree

                            if (canJump) {
                                activeHero.stamina -= 1
                                activeHero.moveDuration = 350
                                activeHero.currentAnim = CharacterAnimation.JUMP
                                activeHero.animationStartTime = gameTimeMillis
                                activeHero.playerX = targetX
                                activeHero.isMoving = true
                                soundPlayer.playSound(GameSound.JUMP, activeHero.heroType)

                                coroutineScope.launch {
                                    activeHero.jumpOffsetY.animateTo(
                                        -250f,
                                        tween(175, easing = FastOutSlowInEasing)
                                    )
                                    activeHero.jumpOffsetY.animateTo(
                                        0f,
                                        tween(175, easing = LinearOutSlowInEasing)
                                    )
                                }
                            }
                        }
                    },
                    onExtractClick = {
                        val targetRes = currentTile.requiredResource

                        if (targetRes != null && activeHero.heroType.extractableResource == targetRes) {
                            // 👍 УСПЕХ
                            activeHero.animationStartTime = gameTimeMillis
                            activeHero.stamina -= 1
                            activeHero.currentAnim = CharacterAnimation.EXTRACT
                            gatheredResources[targetRes] = (gatheredResources[targetRes] ?: 0) + 1
                            soundPlayer.playSound(GameSound.EXTRACT, activeHero.heroType)

                            // 🔥 НОВОЕ: Истощаем жилу, добавляя её координаты в набор!
                            depletedTiles.value = depletedTiles.value + Pair(activeHero.playerX, activeHero.playerY)
                        } else {
                            // ❌ ОТКАЗ
                            activeHero.animationStartTime = gameTimeMillis
                            activeHero.currentAnim = CharacterAnimation.REFUSE
                        }
                    }
                )
            }
        }
    }
}