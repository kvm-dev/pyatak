package ru.mykvm.pyatak.base.engine.utils

import androidx.compose.ui.graphics.ImageBitmap

expect fun String.decodeBase64ToBitmap(): ImageBitmap?
