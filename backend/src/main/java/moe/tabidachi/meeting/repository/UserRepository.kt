package moe.tabidachi.meeting.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.tabidachi.meeting.database.entity.UserEntity
import moe.tabidachi.meeting.database.model.User
import moe.tabidachi.meeting.database.table.UserTable
import moe.tabidachi.meeting.mapper.DomainMapper
import moe.tabidachi.meeting.mapper.UserDomainMapper
import moe.tabidachi.meeting.security.Encryptor
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.Clock

interface UserRepository {
    suspend fun getByEmail(email: String): User?
    suspend fun getByUsername(username: String): User?
    suspend fun create(username: String, email: String, password: String): Long
}

class UserRepositoryImpl(
    private val database: Database,
    private val userDomainMapper: DomainMapper<UserEntity, User> = UserDomainMapper,
    private val encryptor: Encryptor
) : UserRepository {
    private suspend fun <T> withTransaction(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                suspendTransaction(db = database) { block() }
            } catch (e: Exception) {
                throw e
            }
        }

    override suspend fun getByEmail(email: String): User? = withTransaction {
        UserEntity.find { UserTable.email.eq(email) }
            .singleOrNull()
            ?.let(userDomainMapper::toDomain)
    }

    override suspend fun getByUsername(username: String): User? = withTransaction {
        UserEntity.find { UserTable.username.eq(username) }
            .singleOrNull()
            ?.let(userDomainMapper::toDomain)
    }

    override suspend fun create(username: String, email: String, password: String): Long =
        withTransaction {
            val entity = UserEntity.new {
                this.username = username
                this.email = email
                this.password = encryptor.hash(password.toCharArray())
                this.createTime = Clock.System.now()
                this.updateTime = Clock.System.now()
            }
            entity.id.value
        }
}
