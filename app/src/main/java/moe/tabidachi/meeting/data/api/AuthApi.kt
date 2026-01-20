package moe.tabidachi.meeting.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.model.SignupRequest
import moe.tabidachi.meeting.service.AuthService

class AuthApi(
    private val client: HttpClient,
    private val baseUrl: () -> String
) : AuthService {
    override suspend fun signup(request: SignupRequest): Response<String?> =
        client.post(baseUrl()) {
            url {
                appendPathSegments("/signup")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun login(request: LoginRequest): Response<String?> =
        client.post(baseUrl()) {
            url {
                appendPathSegments("/login")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}