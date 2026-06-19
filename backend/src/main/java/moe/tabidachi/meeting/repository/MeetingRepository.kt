package moe.tabidachi.meeting.repository

import io.ktor.util.generateNonce
import moe.tabidachi.meeting.database.entity.DirectMeetingEntity
import moe.tabidachi.meeting.database.entity.MeetingEntity
import moe.tabidachi.meeting.database.entity.UserEntity
import moe.tabidachi.meeting.database.table.DirectMeetingTable
import moe.tabidachi.meeting.ktx.withTransaction
import moe.tabidachi.meeting.mapper.MeetingMapper
import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.model.DirectMeeting
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.MeetingStatus
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SizedCollection
import kotlin.time.Clock

interface MeetingRepository {
    suspend fun create(creatorId: Long, request: CreateMeetingRequest): Meeting
    suspend fun getMeetingsByUserId(userId: Long): List<Meeting>
    suspend fun getOrCreateDirectMeeting(userId: Long, meetingId: Long): DirectMeeting?
}

class MeetingRepositoryImpl(
    private val database: Database,
) : MeetingRepository {
    override suspend fun create(creatorId: Long, request: CreateMeetingRequest): Meeting = database.withTransaction {
        val meetingEntity = MeetingEntity.new {
            this.name = request.name
            this.description = request.description
            this.time = request.time
            this.duration = request.duration
            this.status = MeetingStatus.Upcoming
            this.creatorId = creatorId
            this.createdAt = Clock.System.now()
            this.updatedAt = Clock.System.now()
            val users = (request.participants + creatorId).toSet().mapNotNull {
                UserEntity.findById(it)
            }
            this.participants = SizedCollection(users)
        }
        MeetingMapper.toMeeting(meetingEntity)
    }

    override suspend fun getMeetingsByUserId(userId: Long): List<Meeting> = database.withTransaction {
        UserEntity
            .findById(userId)
            ?.meetings
            ?.map(MeetingMapper::toMeeting)
            ?: emptyList()
    }

    override suspend fun getOrCreateDirectMeeting(
        userId: Long,
        meetingId: Long
    ): DirectMeeting? {
        if (MeetingEntity.findById(meetingId)?.creatorId != userId) {
            return null
        }
        val entity = DirectMeetingEntity
            .find { DirectMeetingTable.meetingId eq meetingId }
            .singleOrNull()
            ?: DirectMeetingEntity.new {
                this.meetingId = meetingId
                this.token = generateNonce()
            }
        return DirectMeeting(
            meetingId = entity.meetingId,
            token = entity.token
        )
    }
}