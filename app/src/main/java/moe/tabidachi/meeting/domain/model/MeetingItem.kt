package moe.tabidachi.meeting.domain.model

import moe.tabidachi.meeting.data.model.Meeting
import moe.tabidachi.meeting.model.UserInfo
import kotlin.time.Duration
import kotlin.time.Instant

data class MeetingItem(
    val id: Long,
    val name: String,
    val time: Instant,
    val duration: Duration,
    val participants: List<UserInfo>,
    val status: Meeting.Status
)