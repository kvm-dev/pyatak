package ru.mykvm.pyatak.base.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareUtils{
    actual fun shareLink(url: String) {
        val activityItems = mutableListOf<Any>()
        activityItems.add(url)
        activityItems.add("foolStack")
        val activityController =
            UIActivityViewController(activityItems = activityItems, applicationActivities = null)

        // Present the activity view controller
        val currentViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        currentViewController?.presentViewController(activityController, animated = true, completion = null)
    }
}