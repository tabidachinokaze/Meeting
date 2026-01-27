package moe.tabidachi.meeting.database.table

import moe.tabidachi.meeting.model.MeetingStatus
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.duration
import org.jetbrains.exposed.v1.datetime.timestamp

object MeetingTable : LongIdTable() {
    val name = text("name")
    val description = text("description").nullable()
    val time = timestamp("time")
    val duration = duration("duration")
    val status = enumeration<MeetingStatus>("status")
    val creatorId = long("creator_id").references(UserTable.id)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
