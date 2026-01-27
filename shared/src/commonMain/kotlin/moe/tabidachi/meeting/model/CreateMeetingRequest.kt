package moe.tabidachi.meeting.model

import kotlin.time.Duration
import kotlin.time.Instant

data class CreateMeetingRequest(
    val name: String,
    val time: Instant,
    val duration: Duration,
    val participants: List<Long>,
)
