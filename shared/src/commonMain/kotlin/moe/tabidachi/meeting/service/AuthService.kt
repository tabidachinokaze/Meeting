package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.model.SignupRequest

interface AuthService {
    suspend fun signup(request: SignupRequest): Response<String?>
    suspend fun login(request: LoginRequest): Response<String?>
}