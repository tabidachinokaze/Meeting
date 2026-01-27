package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.Response

interface MeetingService {
    fun getMeetings(uid: Long): Response<List<Meeting>>
    fun createMeeting(request: CreateMeetingRequest): Response<Unit>
}