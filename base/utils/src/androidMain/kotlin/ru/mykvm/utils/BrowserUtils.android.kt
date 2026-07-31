package ru.mykvm.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class BrowserUtils(val platformContext: Context){
    actual fun openInBrowser(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        platformContext.startActivity(browserIntent)
    }
}