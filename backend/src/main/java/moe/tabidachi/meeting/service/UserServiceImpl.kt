package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.*
import moe.tabidachi.meeting.repository.UserRepository

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override suspend fun getUserInfo(uid: Long, self: Boolean): Response<UserInfo?> {
        val userInfo = userRepository.getUserInfo(uid)
        return if (userInfo == null) {
            StatusCode.UserNotFound.withData(userInfo)
        } else {
            val sensitiveUserInfo = if (self) {
                userInfo
            } else {
                userInfo.copy(
                    email = null,
                    phone = null,
                    createTime = null,
                    updateTime = null
                )
            }
            StatusCode.Success.withData(sensitiveUserInfo)
        }
    }
}