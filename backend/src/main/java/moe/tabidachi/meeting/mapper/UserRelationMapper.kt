package moe.tabidachi.meeting.mapper

import moe.tabidachi.meeting.database.entity.UserRelationEntity
import moe.tabidachi.meeting.database.model.UserRelation

object UserRelationMapper {
    fun toUserRelation(entity: UserRelationEntity): UserRelation {
        return UserRelation(
            userId = entity.userId,
            targetUserId = entity.targetUserId,
            type = entity.type,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}