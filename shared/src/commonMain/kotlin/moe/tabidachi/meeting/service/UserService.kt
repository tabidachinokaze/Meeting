package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.model.UserInfo

interface UserService {
    suspend fun getUserInfo(uid: Long, self: Boolean): Response<UserInfo?>
    suspend fun getContracts(uid: Long): Response<List<UserInfo>>
}

interface UserClientApi {
    suspend fun getUserInfo(uid: Long): Response<UserInfo?>
    suspend fun getContracts(): Response<List<UserInfo>>
}