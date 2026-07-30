package ru.mykvm.language.imp.mapper

import ru.mykvm.language.api.model.CurrentLanguageDomain

object Mapper {

    fun mapStringToCurrentLanguage(local: String): CurrentLanguageDomain {
        return if(local.lowercase().contains("ru")){
            CurrentLanguageDomain.RU
        }else{
            CurrentLanguageDomain.EN
        }
    }
}