package ru.mykvm.language.imp.di

import org.koin.dsl.module
import ru.mykvm.language.imp.data.datasource.LocaleHelper

actual val languagePlatformModule = module {
    single<LocaleHelper> { LocaleHelper() }
}