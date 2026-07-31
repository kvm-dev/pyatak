package ru.mykvm.utils

import android.content.Context
import android.content.Intent

actual class ShareUtils(val platformContext: Context) {
    actual fun shareLink(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Пятак")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        platformContext.startActivity(shareIntent)
    }
}