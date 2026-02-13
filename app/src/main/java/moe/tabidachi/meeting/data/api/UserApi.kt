package moe.tabidachi.meeting.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.service.UserClientApi

class UserApi(
    private val client: HttpClient,
    private val baseUrl: () -> String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserClientApi {
    override suspend fun getUserInfo(uid: Long): Response<UserInfo?> = withContext(dispatcher) {
        client.get(baseUrl()) {
            url { appendPathSegments("users", "$uid") }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getContracts(): Response<List<UserInfo>> = withContext(dispatcher) {
        client.get(baseUrl()) {
            url { appendPathSegments("contacts") }
            contentType(ContentType.Application.Json)
        }.body()
    }
}