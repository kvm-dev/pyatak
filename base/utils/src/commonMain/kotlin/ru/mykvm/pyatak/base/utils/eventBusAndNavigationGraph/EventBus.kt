package ru.mykvm.pyatak.base.utils.eventBusAndNavigationGraph

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.mykvm.pyatak.base.utils.model.AppGlobalEvents

class EventBus {
    private val _events = MutableSharedFlow<AppGlobalEvents>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun emitEvent(event: AppGlobalEvents) {
        _events.tryEmit(event)
    }

    fun clearEvent() {
        _events.tryEmit(AppGlobalEvents.NONE)
    }
}