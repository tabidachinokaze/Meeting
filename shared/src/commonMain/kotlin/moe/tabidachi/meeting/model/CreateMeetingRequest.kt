package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Instant

@Serializable
data class CreateMeetingRequest(
    val name: String,
    val description: String,
    val time: Instant,
    val duration: Duration,
    val participants: List<Long>,
)
