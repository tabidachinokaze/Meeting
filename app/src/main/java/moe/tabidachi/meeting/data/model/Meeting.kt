package moe.tabidachi.meeting.data.model

import kotlin.time.Duration
import kotlin.time.Instant

data class Meeting(
    val id: Long,
    val name: String,
    val time: Instant,
    val duration: Duration,
    val participants: List<Long>,
    val status: Status
) {
    enum class Status {
        Upcoming, Completed
    }
}
