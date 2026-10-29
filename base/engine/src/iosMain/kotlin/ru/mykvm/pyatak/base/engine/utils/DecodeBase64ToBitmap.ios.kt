package ru.mykvm.pyatak.base.engine.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import kotlin.io.encoding.Base64

actual fun String.decodeBase64ToBitmap(): ImageBitmap? {
    return try{
        Image.makeFromEncoded(Base64.decode(this)).toComposeImageBitmap()
    } catch (_: Exception){
        null
    }
}