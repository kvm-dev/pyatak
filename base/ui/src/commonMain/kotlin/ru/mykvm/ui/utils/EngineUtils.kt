package ru.mykvm.ui.utils

import kotlin.math.abs

fun Int.coerceAtAbs(): Int {
    return abs(this)
}