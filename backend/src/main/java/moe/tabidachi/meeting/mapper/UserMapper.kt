package moe.tabidachi.meeting.mapper

import moe.tabidachi.meeting.database.entity.UserEntity
import moe.tabidachi.meeting.database.model.User
import moe.tabidachi.meeting.model.UserInfo

object UserMapper {
    fun toUser(entity: UserEntity): User {
        return User(
            uid = entity.id.value,
            username = entity.username,
            password = entity.password,
            email = entity.email,
            phone = entity.phone,
            avatar = entity.avatar,
            createTime = entity.createTime,
            updateTime = entity.updateTime
        )
    }

    fun toUserInfo(entity: UserEntity): UserInfo {
        return UserInfo(
            uid = entity.id.value,
            username = entity.username,
            email = entity.email,
            phone = entity.phone,
            avatar = entity.avatar,
            createTime = entity.createTime,
            updateTime = entity.updateTime
        )
    }
}
