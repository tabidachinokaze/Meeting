package moe.tabidachi.meeting.model

import kotlin.time.Duration
import kotlin.time.Instant

data class Meeting(
    val id: Long,
    val name: String,
    val description: String?,
    val time: Instant,
    val duration: Duration,
    val status: MeetingStatus,
    val creatorId: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val participants: List<Long>,
)
