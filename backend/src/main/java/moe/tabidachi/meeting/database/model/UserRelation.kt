package moe.tabidachi.meeting.database.model

import kotlinx.serialization.Serializable
import moe.tabidachi.meeting.model.RelationStatus
import moe.tabidachi.meeting.model.RelationType
import kotlin.time.Instant

@Serializable
data class UserRelation(
    val userId: Long,
    val targetUserId: Long,
    val type: RelationType,
    val status: RelationStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)
