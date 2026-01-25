package moe.tabidachi.meeting.plugins

import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.auth.awscredentials.CredentialsProvider
import aws.smithy.kotlin.runtime.collections.Attributes
import aws.smithy.kotlin.runtime.net.Host
import aws.smithy.kotlin.runtime.net.Scheme
import aws.smithy.kotlin.runtime.net.url.Url
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.di.dependencies
import moe.tabidachi.meeting.config.Argon2Config
import moe.tabidachi.meeting.config.DatabaseConfig
import moe.tabidachi.meeting.config.JwtConfig
import moe.tabidachi.meeting.config.MinioConfig
import moe.tabidachi.meeting.database.table.UserTable
import moe.tabidachi.meeting.repository.UserRepository
import moe.tabidachi.meeting.repository.UserRepositoryImpl
import moe.tabidachi.meeting.security.Argon2Encryptor
import moe.tabidachi.meeting.security.Encryptor
import moe.tabidachi.meeting.security.Jwt
import moe.tabidachi.meeting.security.JwtImpl
import moe.tabidachi.meeting.service.AuthService
import moe.tabidachi.meeting.service.AuthServiceImpl
import moe.tabidachi.meeting.service.UserService
import moe.tabidachi.meeting.service.UserServiceImpl
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDI() {
    val argon2Config = property<Argon2Config>("argon2")
    val jwtConfig = property<JwtConfig>("jwt")
    val minioConfig = property<MinioConfig>("minio")
    dependencies {
        provide<Encryptor> {
            Argon2Encryptor(
                iterations = argon2Config.iterations,
                memory = argon2Config.memory,
                parallelism = argon2Config.parallelism
            )
        }
        provide<Jwt> {
            JwtImpl(
                secret = jwtConfig.secret,
                issuer = jwtConfig.issuer,
                audience = arrayOf(jwtConfig.audience)
            )
        }
        provide<S3Client> {
            S3Client {
                this.region = "us-east-1"
                this.endpointUrl = Url {
                    this.scheme = Scheme.HTTP
                    this.host = Host.Domain(minioConfig.host)
                    this.port = minioConfig.port
                }
                this.credentialsProvider = object : CredentialsProvider {
                    override suspend fun resolve(attributes: Attributes): Credentials {
                        return Credentials(
                            accessKeyId = minioConfig.accessKey,
                            secretAccessKey = minioConfig.secretKey
                        )
                    }
                }
            }
        }
    }
    val mode = if (property<Boolean>("ktor.development")) "test" else "main"
    val (url, user, driver, password) = property<DatabaseConfig>("database.$mode")
    dependencies {
        provide<Database> {
            Database.connect(url, driver, user, password).also { db ->
                transaction(db) {
                    SchemaUtils.create(UserTable)
                }
            }
        }
    }
    dependencies {
        provide<UserRepository> {
            UserRepositoryImpl(
                database = resolve(),
                encryptor = resolve()
            )
        }
    }
    dependencies {
        provide<AuthService> {
            AuthServiceImpl(
                jwt = resolve(),
                encryptor = resolve(),
                userRepository = resolve()
            )
        }
        provide<UserService> {
            UserServiceImpl(
                userRepository = resolve()
            )
        }
    }
}
