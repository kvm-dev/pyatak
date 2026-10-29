package ru.mykvm.pyatak.base.engine.model

import org.jetbrains.compose.resources.DrawableResource

data class AnimationConfig(
    val drawable: DrawableResource,
    val frameCount: Int,
    val framesPerRow: Int = 5,
    val durationMillis: Int,
    val loop: Boolean
)