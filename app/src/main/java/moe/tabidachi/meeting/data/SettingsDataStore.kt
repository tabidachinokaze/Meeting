package moe.tabidachi.meeting.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.tabidachi.meeting.BuildConfig
import moe.tabidachi.meeting.data.model.Settings

class SettingsDataStore(
    private val dataStore: DataStore<Settings>,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    val settings: StateFlow<Settings> = dataStore.data.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking { dataStore.data.first() }
    )

    init {
        if (settings.value.baseUrl.isBlank()) {
            val baseUrl =
                if (BuildConfig.DEBUG) "http://tabidachi.lan:23333" else "http://tabidachi.moe:23333"
            scope.launch { update { it.copy(baseUrl = baseUrl) } }
        }
    }

    val token: String? get() = with(settings.value) { tokens[uid] }

    suspend fun setToken(uid: Long, token: String?) {
        update { it.copy(tokens = it.tokens + (uid to token)) }
    }

    suspend fun switchUser(uid: Long) {
        update { it.copy(uid = uid) }
    }

    suspend fun update(transform: suspend (t: Settings) -> Settings) {
        dataStore.updateData(transform)
    }
}