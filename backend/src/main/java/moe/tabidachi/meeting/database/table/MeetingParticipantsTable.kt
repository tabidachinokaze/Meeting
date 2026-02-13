package moe.tabidachi.meeting.database.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object MeetingParticipantsTable : Table("meeting_participants") {
    val meeting = reference(
        name = "meeting",
        foreign = MeetingTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val user = reference(
        name = "user",
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    override val primaryKey: PrimaryKey = PrimaryKey(meeting, user)
}