package moe.tabidachi.meeting.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Debounce(
    private val coroutineScope: CoroutineScope,
    private val isProcessing: () -> Boolean,
    private val onProcessingStateChange: (isProcessing: Boolean) -> Unit,
) {
    private val scope = DebounceScope()

    fun whenIdle(block: suspend DebounceScope.() -> Unit) {
        if (scope.isProcessing) return
        coroutineScope.launch {
            scope.startProcessing()
            scope.block()
            scope.stopProcessing()
        }
    }

    inner class DebounceScope {
        val isProcessing get() = this@Debounce.isProcessing()

        fun startProcessing() {
            onProcessingStateChange(true)
        }

        fun stopProcessing() {
            onProcessingStateChange(false)
        }
    }
}