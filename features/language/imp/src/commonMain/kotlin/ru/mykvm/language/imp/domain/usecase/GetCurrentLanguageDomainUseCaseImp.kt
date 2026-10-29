package ru.mykvm.language.imp.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.mykvm.language.api.domain.usecase.GetCurrentLanguageUseCase
import ru.mykvm.language.api.model.CurrentLanguageDomain
import ru.mykvm.language.imp.data.LanguageRepository
import ru.mykvm.language.imp.mapper.Mapper
import ru.mykvm.pyatak.base.utils.model.ResultState

class GetCurrentLanguageUseCaseImp(private val repository: LanguageRepository):
    GetCurrentLanguageUseCase {

    private val _langState = MutableStateFlow<ResultState<CurrentLanguageDomain>>(
        ResultState.Idle
    )
    override val langState = _langState.asStateFlow()

    override fun getLang(): CurrentLanguageDomain {
        _langState.tryEmit(ResultState.Loading)
        val state = Mapper.mapStringToCurrentLanguage(repository.getLanguage())
        _langState.tryEmit(ResultState.Success(state))
        return state
    }
}