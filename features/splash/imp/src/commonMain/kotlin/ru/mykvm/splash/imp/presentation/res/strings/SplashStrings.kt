package ru.mykvm.splash.imp.presentation.res.strings

import ru.mykvm.language.api.model.CurrentLanguageDomain

fun getSplashTitle(lang: CurrentLanguageDomain): String{
    return when(lang){
        CurrentLanguageDomain.RU -> "Пятак"
        CurrentLanguageDomain.EN -> "Pyatak"

    }
}

fun getSplashDescription(lang: CurrentLanguageDomain): String{
    return when(lang){
        CurrentLanguageDomain.RU -> "Свинячая угроза"
        CurrentLanguageDomain.EN -> "The pig menace"

    }
}