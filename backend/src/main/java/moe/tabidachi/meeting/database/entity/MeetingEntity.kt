package moe.tabidachi.meeting.database.entity

import moe.tabidachi.meeting.database.table.MeetingParticipantsTable
import moe.tabidachi.meeting.database.table.MeetingTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class MeetingEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MeetingEntity>(MeetingTable)

    var name by MeetingTable.name
    var description by MeetingTable.description
    var time by MeetingTable.time
    var duration by MeetingTable.duration
    var status by MeetingTable.status
    var creatorId by MeetingTable.creatorId
    var createdAt by MeetingTable.createdAt
    var updatedAt by MeetingTable.updatedAt

    val participants by UserEntity via MeetingParticipantsTable
}
