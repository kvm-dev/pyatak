package ru.mykvm.ui.enemy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.mykvm.ui.model.AnimationConfig
import ru.mykvm.ui.model.EnemyAnimation

class EnemyState(
    val enemyType: EnemyType,
    startX: Int,
    startY: Int = 0,
    val patrolRadius: Int = 2 // На сколько клеток влево-вправо враг может отходить
) {
    var renderOffsetX by mutableStateOf(0f)
    var x by mutableStateOf(startX)
    var y by mutableStateOf(startY)

    val spawnX = startX // Запоминаем точку спавна, чтобы не уходить от нее далеко

    var health by mutableStateOf(enemyType.maxHealth)
    var currentAnim by mutableStateOf(EnemyAnimation.IDLE)
    var isLeft by mutableStateOf(true)

    var isMoving by mutableStateOf(false)
    var animationStartTime by mutableStateOf(0L)

    // Таймер блокировки ИИ (например, когда враг получил урон и застыл в анимации SPECIAL)
    var aiStunDurationMillis by mutableStateOf(0L)

    var jumpOffsetY by mutableStateOf(0f)

    var isBossSecondPhase by mutableStateOf(false)
    var hasResurrected by mutableStateOf(false)

    val currentConfig: AnimationConfig
        get() = enemyType.animations[currentAnim] ?: enemyType.animations[EnemyAnimation.IDLE]!!
}