package moe.tabidachi.meeting.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val uid: Long,
    val tokens: Map<Long, String?>,
    val baseUrl: String = ""
) {
    companion object {
        val Empty = Settings(
            uid = 0,
            tokens = emptyMap()
        )
    }
}