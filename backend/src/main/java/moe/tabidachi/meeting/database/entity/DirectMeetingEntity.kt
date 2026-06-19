package moe.tabidachi.meeting.database.entity

import moe.tabidachi.meeting.database.table.DirectMeetingTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

class DirectMeetingEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<DirectMeetingEntity, DirectMeetingTable>(DirectMeetingTable)

    var meetingId by DirectMeetingTable.meetingId
    var token by DirectMeetingTable.token
}