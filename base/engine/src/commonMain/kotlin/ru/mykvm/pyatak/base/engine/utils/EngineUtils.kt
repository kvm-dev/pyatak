package ru.mykvm.pyatak.base.engine.utils

import kotlin.math.abs

fun Int.coerceAtAbs(): Int {
    return abs(this)
}
