package moe.tabidachi.meeting.repository

import moe.tabidachi.meeting.database.entity.UserRelationEntity
import moe.tabidachi.meeting.database.model.UserRelation
import moe.tabidachi.meeting.database.table.UserRelationTable
import moe.tabidachi.meeting.ktx.withTransaction
import moe.tabidachi.meeting.mapper.UserRelationMapper
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

interface UserRelationRepository {
    suspend fun getByUserId(userId: Long): List<UserRelation>
}

class UserRelationRepositoryImpl(
    private val database: Database
) : UserRelationRepository {
    override suspend fun getByUserId(userId: Long): List<UserRelation> = database.withTransaction {
        UserRelationEntity.find { UserRelationTable.userId.eq(userId) }
            .map(UserRelationMapper::toUserRelation)
    }
}
