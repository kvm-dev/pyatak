package ru.mykvm.pyatak.base.utils.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mykvm.pyatak.base.utils.BrowserUtils
import ru.mykvm.pyatak.base.utils.ShareUtils
import ru.mykvm.pyatak.base.utils.eventBusAndNavigationGraph.EventBus

actual val utilsModule: Module
    get() =  module {
        single<EventBus> { EventBus() }
        single<BrowserUtils> { BrowserUtils() }
        single<ShareUtils> { ShareUtils() }
    }