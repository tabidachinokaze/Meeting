package moe.tabidachi.meeting.ktx

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import moe.tabidachi.meeting.data.model.Settings
import moe.tabidachi.meeting.serializer.SettingsSerializer

val Context.dataStore: DataStore<Settings> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer,
)