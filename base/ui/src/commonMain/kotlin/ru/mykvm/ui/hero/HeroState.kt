package ru.mykvm.ui.hero

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.mykvm.ui.model.AnimationConfig
import ru.mykvm.ui.model.CharacterAnimation

class HeroState(
    val heroClass: HeroClass,
    startX: Int,
    startHealth: Int = 5,
    startStamina: Int = 10
) {
    var playerX by mutableStateOf(startX)
    var playerY by mutableStateOf(0) // На случай, если в будущем будут разные этажи

    var currentAnim by mutableStateOf(CharacterAnimation.IDLE)
    var isLeft by mutableStateOf(false)

    var isMoving by mutableStateOf(false)
    var isFalling by mutableStateOf(false)

    var health by mutableStateOf(startHealth)
    var stamina by mutableStateOf(startStamina)

    val jumpOffsetY = Animatable(0f)
    var moveDuration by mutableStateOf(300)

    var animationStartTime by mutableStateOf(0L)

    // Вспомогательный метод для быстрого получения текущего конфига анимации
    val currentConfig: AnimationConfig
        get() = heroClass.animations[currentAnim] ?: heroClass.animations[CharacterAnimation.IDLE]!!
}