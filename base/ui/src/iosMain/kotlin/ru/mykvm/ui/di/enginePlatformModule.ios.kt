package ru.mykvm.ui.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mykvm.ui.utils.SoundPlayer

actual val enginePlatformModule: Module = module {
    single { SoundPlayer() }
}