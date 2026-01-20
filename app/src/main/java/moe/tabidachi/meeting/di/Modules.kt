package moe.tabidachi.meeting.di

import android.content.Context
import androidx.datastore.core.DataStore
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.AuthApi
import moe.tabidachi.meeting.data.model.Settings
import moe.tabidachi.meeting.ktx.dataStore
import moe.tabidachi.meeting.shared.SharedHttpClient
import moe.tabidachi.meeting.shared.SharedJson
import moe.tabidachi.meeting.ui.auth.auth
import org.koin.dsl.module

val routeModule = module {
    auth()
}

val appModule = module {
    single<DataStore<Settings>> {
        val context: Context = get()
        context.dataStore
    }
    single {
        SettingsDataStore(dataStore = get())
    }
    single<Json> {
        SharedJson()
    }
    single<HttpClient> {
        val dataStore: SettingsDataStore = get()
        SharedHttpClient(
            json = get(),
            tokenProvider = { dataStore.token }
        )
    }
    single<AuthApi> {
        val dataStore: SettingsDataStore = get()
        AuthApi(
            client = get(),
            baseUrl = { dataStore.settings.value.baseUrl }
        )
    }
}