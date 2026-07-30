package ru.mykvm.language.imp.data.datasource

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

actual class LocaleHelper {
    actual fun getCurrentLocale(): String {
        return NSLocale.currentLocale.localeIdentifier
    }
}