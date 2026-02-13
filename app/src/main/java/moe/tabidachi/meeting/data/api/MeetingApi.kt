package moe.tabidachi.meeting.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.service.MeetingClientApi

class MeetingApi(
    private val client: HttpClient,
    private val baseUrl: () -> String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MeetingClientApi {
    override suspend fun getMeetings(): Response<List<Meeting>?> = withContext(dispatcher) {
        client.get(baseUrl()) {
            url { appendPathSegments("meetings") }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun createMeeting(request: CreateMeetingRequest): Response<Meeting?> =
        withContext(dispatcher) {
            client.post(baseUrl()) {
                url { appendPathSegments("meetings") }
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
}