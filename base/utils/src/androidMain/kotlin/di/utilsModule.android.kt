package di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.mykvm.utils.BrowserUtils
import ru.mykvm.utils.ShareUtils
import ru.mykvm.utils.eventBusAndNavigationGraph.EventBus

actual val utilsModule: Module
    get() =  module {
        single<EventBus> { EventBus() }
        single<BrowserUtils> { BrowserUtils(platformContext = androidContext()) }
        single<ShareUtils> { ShareUtils(platformContext = androidContext()) }
    }