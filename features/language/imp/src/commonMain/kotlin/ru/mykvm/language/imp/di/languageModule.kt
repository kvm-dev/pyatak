package ru.mykvm.language.imp.di

import org.koin.dsl.module
import ru.mykvm.language.api.domain.usecase.GetCurrentLanguageUseCase
import ru.mykvm.language.imp.data.LanguageRepository
import ru.mykvm.language.imp.data.datasource.DataSource
import ru.mykvm.language.imp.domain.usecase.GetCurrentLanguageUseCaseImp

val languageModule = module {
    single<DataSource> { DataSource (helper = get()) }
    single<LanguageRepository> { LanguageRepository(dataSource = get()) }
    single<GetCurrentLanguageUseCase> { GetCurrentLanguageUseCaseImp(repository = get()) }
}