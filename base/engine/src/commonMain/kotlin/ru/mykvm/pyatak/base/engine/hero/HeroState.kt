package ru.mykvm.pyatak.base.engine.hero

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.mykvm.pyatak.base.engine.model.AnimationConfig
import ru.mykvm.pyatak.base.engine.model.CharacterAnimation

class HeroState(
    val heroType: HeroType,
    startX: Int,
    health: Int = 5,
    stamina: Int = 10
) {
    var playerX by mutableStateOf(startX)
    var playerY by mutableStateOf(0) // На случай, если в будущем будут разные этажи

    var currentAnim by mutableStateOf(CharacterAnimation.IDLE)
    var isLeft by mutableStateOf(false)

    var isMoving by mutableStateOf(false)
    var isFalling by mutableStateOf(false)

    var health by mutableStateOf(health)
    var stamina by mutableStateOf(stamina)

    var maxStamina by mutableStateOf(stamina)

    val jumpOffsetY = Animatable(0f)
    var moveDuration by mutableStateOf(300)

    var animationStartTime by mutableStateOf(0L)

    // Вспомогательный метод для быстрого получения текущего конфига анимации
    val currentConfig: AnimationConfig
        get() = heroType.animations[currentAnim] ?: heroType.animations[CharacterAnimation.IDLE]!!
}