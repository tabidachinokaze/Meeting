package moe.tabidachi.meeting.database.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object MeetingParticipantsTable : LongIdTable() {
    val meetingId = long("meeting_id").references(
        ref = MeetingTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val userId = long("user_id").references(
        ref = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    init {
        uniqueIndex(meetingId, userId)
    }
}