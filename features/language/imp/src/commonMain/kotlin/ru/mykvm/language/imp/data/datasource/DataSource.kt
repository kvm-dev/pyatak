package ru.mykvm.language.imp.data.datasource

class DataSource(private val helper: LocaleHelper) {
    fun getLang(): String {
        return helper.getCurrentLocale()
    }
}