package moe.tabidachi.compose.mvi

import androidx.lifecycle.ViewModel

public abstract class BackingFieldsViewModel<STATE, EVENT, EFFECT>() : ViewModel(), UnidirectionalViewModel<STATE, EVENT, EFFECT> {
    private val handledOneTimeEvents: MutableSet<EVENT> = mutableSetOf()

    protected fun handleOneTimeEvent(event: EVENT, block: () -> Unit) {
        if (event !in handledOneTimeEvents) {
            handledOneTimeEvents.add(event)
            block()
        }
    }
}
