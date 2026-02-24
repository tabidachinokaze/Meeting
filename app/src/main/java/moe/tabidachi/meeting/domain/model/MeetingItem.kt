package moe.tabidachi.meeting.domain.model

import kotlinx.serialization.Serializable
import moe.tabidachi.meeting.model.MeetingStatus
import moe.tabidachi.meeting.model.UserInfo
import kotlin.time.Duration
import kotlin.time.Instant

@Serializable
data class MeetingItem(
    val id: Long,
    val name: String,
    val time: Instant,
    val duration: Duration,
    val participants: List<UserInfo>,
    val status: MeetingStatus
)