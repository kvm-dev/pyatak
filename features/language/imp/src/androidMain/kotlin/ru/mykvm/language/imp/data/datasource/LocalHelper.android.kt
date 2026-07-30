package ru.mykvm.language.imp.data.datasource

import java.util.Locale

actual class LocaleHelper {
    actual fun getCurrentLocale(): String {
        return Locale.getDefault().toString()
    }
}