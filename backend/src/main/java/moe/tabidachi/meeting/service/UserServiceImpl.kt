package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.*
import moe.tabidachi.meeting.repository.UserRelationRepository
import moe.tabidachi.meeting.repository.UserRepository

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userRelationRepository: UserRelationRepository
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

    override suspend fun getContracts(uid: Long): Response<List<UserInfo>> {
        val userInfos = userRelationRepository.getByUserId(uid).filter {
            it.status == RelationStatus.ACTIVE
        }.mapNotNull {
            userRepository.getUserInfo(it.targetUserId)
        }
        return StatusCode.Success.withData(userInfos)
    }
}