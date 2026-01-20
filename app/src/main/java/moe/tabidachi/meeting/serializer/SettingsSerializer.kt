package moe.tabidachi.meeting.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import moe.tabidachi.meeting.data.model.Settings
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings.Empty

    override suspend fun readFrom(input: InputStream): Settings = try {
        Json.decodeFromString<Settings>(input.readBytes().decodeToString())
    } catch (serialization: SerializationException) {
        throw CorruptionException("Unable to read Settings", serialization)
    }

    override suspend fun writeTo(
        t: Settings,
        output: OutputStream
    ) {
        output.write(Json.encodeToString(t).encodeToByteArray())
    }
}