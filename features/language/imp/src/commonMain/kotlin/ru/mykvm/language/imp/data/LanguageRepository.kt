package ru.mykvm.language.imp.data

import ru.mykvm.language.imp.data.datasource.DataSource

class LanguageRepository(private val dataSource: DataSource) {

    fun getLanguage(): String {
        return dataSource.getLang()
    }
}