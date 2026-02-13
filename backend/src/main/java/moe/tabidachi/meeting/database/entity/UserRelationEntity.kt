package moe.tabidachi.meeting.database.entity

import moe.tabidachi.meeting.database.table.UserRelationTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserRelationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserRelationEntity>(UserRelationTable)

    var userId by UserRelationTable.userId
    var targetUserId by UserRelationTable.targetUserId
    var type by UserRelationTable.type
    var status by UserRelationTable.status
    var createdAt by UserRelationTable.createdAt
    var updatedAt by UserRelationTable.updatedAt
}
