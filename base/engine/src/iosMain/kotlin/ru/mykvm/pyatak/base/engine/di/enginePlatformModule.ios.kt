package ru.mykvm.pyatak.base.engine.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mykvm.pyatak.base.engine.utils.SoundPlayer

actual val enginePlatformModule: Module = module {
    single { SoundPlayer() }
}