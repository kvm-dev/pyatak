package ru.mykvm.pyatak.base.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class BrowserUtils{
    actual fun openInBrowser(url: String) {
        NSURL.URLWithString(url)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}