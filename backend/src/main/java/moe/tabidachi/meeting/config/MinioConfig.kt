package moe.tabidachi.meeting.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MinioConfig(
    val host: String,
    val port: Int,
    @SerialName("access_key")
    val accessKey: String,
    @SerialName("secret_key")
    val secretKey: String,
)
