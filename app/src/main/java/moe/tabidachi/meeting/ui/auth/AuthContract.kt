package moe.tabidachi.meeting.ui.auth

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.AuthApi
import moe.tabidachi.meeting.jwt.Claims
import moe.tabidachi.meeting.ktx.TAG
import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.SignupRequest
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.regex.RegexEmail
import moe.tabidachi.meeting.regex.RegexUsernameStrict
import moe.tabidachi.meeting.utils.Debounce
import java.nio.channels.UnresolvedAddressException

interface AuthContract {
    abstract class ViewModel() : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val authType: AuthType = AuthType.Login,
        val username: String = "",
        val password: String = "",
        val email: String = "",
        val account: String = "",
        val isPasswordVisible: Boolean = false,
        @StringRes val emailErrorMessage: Int? = null,
        @StringRes val usernameErrorMessage: Int? = null,
        @StringRes val passwordErrorMessage: Int? = null,
        @StringRes val accountErrorMessage: Int? = null,
        val isProcessing: Boolean = false
    )

    data class Actions(
        val onAuthTypeChange: (AuthType) -> Unit = {},
        val onUsernameChange: (String) -> Unit = {},
        val onPasswordChange: (String) -> Unit = {},
        val onEmailChange: (String) -> Unit = {},
        val onAccountChange: (String) -> Unit = {},
        val onPasswordVisibleToggle: () -> Unit = {},
        val onLogin: () -> Unit = {},
        val onSignUp: () -> Unit = {},
        val onForgotPassword: () -> Unit = {},
    )

    sealed interface Event {
        data class OnAuthTypeChange(val authType: AuthType) : Event
        data class OnUsernameChange(val value: String) : Event
        data class OnPasswordChange(val value: String) : Event
        data class OnEmailChange(val value: String) : Event
        data class OnAccountChange(val value: String) : Event
        data object OnPasswordVisibleToggle : Event
        data object OnLogin : Event
        data object OnSignUp : Event
    }

    sealed interface Effect {
        data class Toast(val text: String) : Effect
    }
}

enum class AuthType(
    @StringRes val text: Int,
    @StringRes val subtitle: Int
) {
    Login(R.string.auth_screen_login, R.string.auth_screen_subtitle_login),
    SignUp(R.string.auth_screen_sign_up, R.string.auth_screen_subtitle_sign_up)
}

class AuthViewModel(
    private val context: Context,
    private val authApi: AuthApi,
    private val dataStore: SettingsDataStore
) : AuthContract.ViewModel() {
    final override val state: StateFlow<AuthContract.State>
        field = MutableStateFlow(AuthContract.State())
    final override val effect: SharedFlow<AuthContract.Effect>
        field = MutableSharedFlow<AuthContract.Effect>()

    private val debounce = Debounce(
        coroutineScope = viewModelScope,
        isProcessing = { state.value.isProcessing },
        onProcessingStateChange = { isProcessing ->
            state.update { it.copy(isProcessing = isProcessing) }
        }
    )

    override fun event(event: AuthContract.Event) = when (event) {
        is AuthContract.Event.OnAuthTypeChange ->
            state.update { it.copy(authType = event.authType) }

        is AuthContract.Event.OnEmailChange -> state.update {
            it.copy(email = event.value, emailErrorMessage = null)
        }

        is AuthContract.Event.OnPasswordChange -> state.update {
            it.copy(password = event.value, passwordErrorMessage = null)
        }

        AuthContract.Event.OnPasswordVisibleToggle ->
            state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        is AuthContract.Event.OnUsernameChange -> state.update {
            it.copy(username = event.value, usernameErrorMessage = null)
        }

        AuthContract.Event.OnLogin -> onLogin()
        AuthContract.Event.OnSignUp -> onSignUp()
        is AuthContract.Event.OnAccountChange -> state.update {
            it.copy(account = event.value, accountErrorMessage = null)
        }
    }

    private fun onLogin() = debounce.whenIdle {
        when {
            state.value.account.isBlank() -> state.update {
                it.copy(accountErrorMessage = R.string.auth_screen_error_account_empty)
            }

            state.value.password.length < 8 -> state.update {
                it.copy(passwordErrorMessage = R.string.auth_screen_error_password_length)
            }

            else -> {
                runCatching {
                    val response = authApi.login(
                        request = LoginRequest(
                            account = state.value.account,
                            password = state.value.password
                        )
                    )
                    Log.d(TAG, "onLogin: ${StatusCode.valueOf(response.code)}")
                    when (StatusCode.valueOf(response.code)) {
                        StatusCode.UserNotRegistered -> state.update {
                            it.copy(accountErrorMessage = R.string.auth_screen_error_user_not_registered)
                        }

                        StatusCode.PasswordMismatch -> state.update {
                            it.copy(passwordErrorMessage = R.string.auth_screen_error_password_mismatch)
                        }

                        StatusCode.InternalError -> effect.emit(
                            AuthContract.Effect.Toast(context.getString(R.string.auth_screen_error_server_internal))
                        )

                        StatusCode.LoginSuccess -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.auth_screen_success_login)))
                            response.data?.let { updateToken(it) }
                        }

                        else -> Unit
                    }
                }.onFailure {
                    when (it) {
                        is UnresolvedAddressException -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.error_network_address)))
                        }

                        is ConnectTimeoutException -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.error_connect_timeout)))
                        }
                    }
                    Log.e(TAG, "onLogin: ", it)
                }
            }
        }
    }

    private fun onSignUp() = debounce.whenIdle {
        when {
            !RegexUsernameStrict.matches(state.value.username) -> state.update {
                it.copy(usernameErrorMessage = R.string.auth_screen_error_invalid_username)
            }

            !RegexEmail.matches(state.value.email) -> state.update {
                it.copy(emailErrorMessage = R.string.auth_screen_error_invalid_email)
            }

            state.value.password.length < 8 -> state.update {
                it.copy(passwordErrorMessage = R.string.auth_screen_error_password_length)
            }

            else -> {
                runCatching {
                    val response = authApi.signup(
                        request = SignupRequest(
                            username = state.value.username,
                            email = state.value.email,
                            password = state.value.password
                        )
                    )
                    when (StatusCode.valueOf(response.code)) {
                        StatusCode.EmailAlreadyExists -> state.update {
                            it.copy(emailErrorMessage = R.string.auth_screen_error_email_registered)
                        }

                        StatusCode.UsernameAlreadyExists -> state.update {
                            it.copy(usernameErrorMessage = R.string.auth_screen_error_username_taken)
                        }

                        StatusCode.PasswordTooWeak -> state.update {
                            it.copy(passwordErrorMessage = R.string.auth_screen_error_password_length)
                        }

                        StatusCode.InvalidEmail -> state.update {
                            it.copy(emailErrorMessage = R.string.auth_screen_error_invalid_email)
                        }

                        StatusCode.InvalidUsername -> state.update {
                            it.copy(usernameErrorMessage = R.string.auth_screen_error_invalid_username)
                        }

                        StatusCode.InternalError -> effect.emit(
                            AuthContract.Effect.Toast(context.getString(R.string.auth_screen_error_server_internal))
                        )

                        StatusCode.SignUpSuccess -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.auth_screen_success_register)))
                            response.data?.let { updateToken(it) }
                        }

                        else -> Unit
                    }
                }.onFailure {
                    when (it) {
                        is UnresolvedAddressException -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.error_network_address)))
                        }

                        is ConnectTimeoutException -> {
                            effect.emit(AuthContract.Effect.Toast(context.getString(R.string.error_connect_timeout)))
                        }
                    }
                    Log.e(TAG, "onSignUp: ", it)
                }
            }
        }
    }

    private suspend fun updateToken(token: String) {
        val jwt = JWT(token)
        val uid = jwt.getClaim(Claims.UID).asLong() ?: error("未解析出uid")
        dataStore.setToken(uid, token)
        dataStore.switchUser(uid)
    }
}
