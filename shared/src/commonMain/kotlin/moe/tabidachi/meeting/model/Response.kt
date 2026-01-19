package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val code: Int,
    val message: String,
    val data: T
)