package moe.tabidachi.meeting.routing

import io.ktor.server.application.log
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.tabidachi.meeting.ktx.requireUserId
import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.service.MeetingService

fun Route.meeting() {
    val meetingService: MeetingService by application.dependencies

    get("/meetings") {
        val userId = requireUserId()
        call.respond(meetingService.getMeetings(userId))
    }
    post<CreateMeetingRequest>("/meetings") { request ->
        val creatorId = requireUserId()
        println(request.toString())
        val response = meetingService.createMeeting(
            creatorId = creatorId,
            request = request
        )
        println(response)
        call.respond(response)
    }
}