package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.*
import moe.tabidachi.meeting.repository.MeetingRepository

class MeetingServiceImpl(
    private val meetingRepository: MeetingRepository,
) : MeetingService {
    override suspend fun getMeetings(uid: Long): Response<List<Meeting>> {
        return StatusCode.Success.withData(meetingRepository.getMeetingsByUserId(uid))
    }

    override suspend fun createMeeting(creatorId: Long, request: CreateMeetingRequest): Response<Meeting> {
        val meeting = meetingRepository.create(
            creatorId = creatorId,
            request = request
        )
        return StatusCode.Success.withData(meeting)
    }
}