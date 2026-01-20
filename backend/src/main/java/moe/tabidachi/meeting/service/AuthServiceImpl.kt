package moe.tabidachi.meeting.service

import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.Response
import moe.tabidachi.meeting.model.SignupRequest
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.model.emptyData
import moe.tabidachi.meeting.model.withData
import moe.tabidachi.meeting.regex.RegexEmail
import moe.tabidachi.meeting.regex.RegexUsernameStrict
import moe.tabidachi.meeting.repository.UserRepository
import moe.tabidachi.meeting.security.Encryptor
import moe.tabidachi.meeting.security.Jwt
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaInstant

class AuthServiceImpl(
    private val jwt: Jwt,
    private val encryptor: Encryptor,
    private val userRepository: UserRepository
) : AuthService {
    override suspend fun signup(request: SignupRequest): Response<String?> {
        return when {
            !RegexEmail.matches(request.email) -> StatusCode.InvalidEmail.emptyData()
            request.password.length < 8 -> StatusCode.PasswordTooWeak.emptyData()
            request.username.length !in 2..32 || !RegexUsernameStrict.matches(request.username) -> StatusCode.InvalidUsername.emptyData()
            userRepository.getByEmail(request.email) != null -> StatusCode.EmailAlreadyExists.emptyData()
            userRepository.getByUsername(request.username) != null -> StatusCode.UsernameAlreadyExists.emptyData()
            else -> {
                val uid = userRepository.create(
                    username = request.username,
                    email = request.email,
                    password = request.password
                )
                val token = jwt.sign(uid) {
                    withExpiresAt(Clock.System.now().plus(7.days).toJavaInstant())
                }
                StatusCode.SignUpSuccess.withData(token)
            }
        }
    }

    override suspend fun login(request: LoginRequest): Response<String?> {
        val isEmail = RegexEmail.matches(request.account)
        val user = if (isEmail) {
            userRepository.getByEmail(request.account)
        } else {
            userRepository.getByUsername(request.account)
        }
        return when (user) {
            null -> StatusCode.UserNotRegistered.emptyData()
            else -> when {
                // !RegexEmail.matches(request.account) -> StatusCode.InvalidEmail.emptyData()
                request.password.length < 8 -> StatusCode.PasswordTooWeak.emptyData()
                !encryptor.verify(
                    user.password,
                    request.password.toCharArray()
                ) -> StatusCode.PasswordMismatch.emptyData()

                else -> {
                    val token = jwt.sign(user.uid) {
                        withExpiresAt(Clock.System.now().plus(7.days).toJavaInstant())
                    }
                    StatusCode.LoginSuccess.withData(token)
                }
            }
        }
    }
}