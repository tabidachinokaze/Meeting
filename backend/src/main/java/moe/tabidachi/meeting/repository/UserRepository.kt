package moe.tabidachi.meeting.repository

import moe.tabidachi.meeting.database.entity.UserEntity
import moe.tabidachi.meeting.database.model.User
import moe.tabidachi.meeting.database.table.UserTable
import moe.tabidachi.meeting.ktx.withTransaction
import moe.tabidachi.meeting.mapper.UserMapper
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.security.Encryptor
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.time.Clock

interface UserRepository {
    suspend fun getByEmail(email: String): User?
    suspend fun getByUsername(username: String): User?
    suspend fun create(username: String, email: String, password: String): Long
    suspend fun getUserInfo(uid: Long): UserInfo?
}

class UserRepositoryImpl(
    private val database: Database,
    private val encryptor: Encryptor
) : UserRepository {
    override suspend fun getByEmail(email: String): User? = database.withTransaction {
        UserEntity.find { UserTable.email.eq(email) }
            .singleOrNull()
            ?.let(UserMapper::toUser)
    }

    override suspend fun getByUsername(username: String): User? = database.withTransaction {
        UserEntity.find { UserTable.username.eq(username) }
            .singleOrNull()
            ?.let(UserMapper::toUser)
    }

    override suspend fun create(username: String, email: String, password: String): Long =
        database.withTransaction {
            val entity = UserEntity.new {
                this.username = username
                this.email = email
                this.password = encryptor.hash(password.toCharArray())
                this.createTime = Clock.System.now()
                this.updateTime = Clock.System.now()
            }
            entity.id.value
        }

    override suspend fun getUserInfo(uid: Long): UserInfo? = database.withTransaction {
        UserEntity.find { UserTable.id.eq(uid) }
            .singleOrNull()
            ?.let(UserMapper::toUserInfo)
    }
}
