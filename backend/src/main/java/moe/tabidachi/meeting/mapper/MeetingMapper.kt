package moe.tabidachi.meeting.mapper

import moe.tabidachi.meeting.database.entity.MeetingEntity
import moe.tabidachi.meeting.model.Meeting

object MeetingMapper {
    fun toMeeting(entity: MeetingEntity): Meeting {
        return Meeting(
            id = entity.id.value,
            name = entity.name,
            description = entity.description,
            time = entity.time,
            duration = entity.duration,
            status = entity.status,
            creatorId = entity.creatorId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            participants = entity.participants.map { it.id.value },
        )
    }
}