package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.Response

interface MeetingService {
    suspend fun getMeetings(uid: Long): Response<List<Meeting>>
    suspend fun createMeeting(creatorId: Long, request: CreateMeetingRequest): Response<Meeting>
}

interface MeetingClientApi {
    suspend fun getMeetings(): Response<List<Meeting>?>
    suspend fun createMeeting(request: CreateMeetingRequest): Response<Meeting?>
}