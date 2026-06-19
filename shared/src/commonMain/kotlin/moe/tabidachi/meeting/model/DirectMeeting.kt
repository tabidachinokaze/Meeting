package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable

@Serializable
data class DirectMeeting(
    val meetingId: Long,
    val token: String
)
