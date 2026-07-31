package ru.mykvm.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
val NoRippleConfiguration = RippleConfiguration(
    color = Color.Unspecified,
    rippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
)