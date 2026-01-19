package moe.tabidachi.meeting.mapper

import moe.tabidachi.meeting.database.entity.UserEntity
import moe.tabidachi.meeting.database.model.User

object UserDomainMapper : DomainMapper<UserEntity, User> {
    override fun toDomain(dto: UserEntity): User {
        return User(
            uid = dto.id.value,
            username = dto.username,
            password = dto.password,
            email = dto.email,
            phone = dto.phone,
            avatar = dto.avatar,
            createTime = dto.createTime,
            updateTime = dto.updateTime
        )
    }
}
