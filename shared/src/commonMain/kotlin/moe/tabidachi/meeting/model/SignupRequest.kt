package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val password: String
)

