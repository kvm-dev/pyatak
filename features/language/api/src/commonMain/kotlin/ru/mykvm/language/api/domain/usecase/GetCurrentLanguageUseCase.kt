package ru.mykvm.language.api.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.mykvm.language.api.model.CurrentLanguageDomain
import ru.mykvm.pyatak.base.utils.model.ResultState

interface GetCurrentLanguageUseCase {

    val langState: StateFlow<ResultState<CurrentLanguageDomain>>
    fun getLang(): CurrentLanguageDomain
}