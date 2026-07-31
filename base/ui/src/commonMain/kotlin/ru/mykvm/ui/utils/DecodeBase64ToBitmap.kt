package ru.mykvm.ui.utils

import androidx.compose.ui.graphics.ImageBitmap

expect fun String.decodeBase64ToBitmap(): ImageBitmap?
