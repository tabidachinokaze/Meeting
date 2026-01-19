package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val account: String,
    val password: String
)