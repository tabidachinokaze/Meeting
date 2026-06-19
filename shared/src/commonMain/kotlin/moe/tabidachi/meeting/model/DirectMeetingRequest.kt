package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable

@Serializable
data class DirectMeetingRequest(
    val meetingId: Long,
)
