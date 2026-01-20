package moe.tabidachi.meeting.ui.auth

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.AuthApi
import moe.tabidachi.meeting.jwt.Claims
import moe.tabidachi.meeting.ktx.TAG
import moe.tabidachi.meeting.model.LoginRequest
import moe.tabidachi.meeting.model.SignupRequest
import moe.tabidachi.meeting.regex.RegexEmail
import moe.tabidachi.meeting.regex.RegexUsernameStrict

interface AuthContract {
    abstract class ViewModel() : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val authType: AuthType = AuthType.Login,
        val username: String = "",
        val password: String = "",
        val email: String = "",
        val account: String = "",
        val isPasswordVisible: Boolean = false,
        val isEmailInvalid: Boolean = false,
        val isUsernameInvalid: Boolean = false,
        val isPasswordInvalid: Boolean = false,
        val isAccountInvalid: Boolean = false,
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

    sealed interface Effect
}

enum class AuthType(
    @StringRes val text: Int,
    @StringRes val subtitle: Int
) {
    Login(R.string.auth_screen_login, R.string.auth_screen_subtitle_login),
    SignUp(R.string.auth_screen_sign_up, R.string.auth_screen_subtitle_sign_up)
}

class AuthViewModel(
    private val authApi: AuthApi,
    private val dataStore: SettingsDataStore
) : AuthContract.ViewModel() {
    final override val state: StateFlow<AuthContract.State>
        field = MutableStateFlow(AuthContract.State())
    final override val effect: SharedFlow<AuthContract.Effect>
        field = MutableSharedFlow<AuthContract.Effect>()

    override fun event(event: AuthContract.Event) = when (event) {
        is AuthContract.Event.OnAuthTypeChange ->
            state.update { it.copy(authType = event.authType) }

        is AuthContract.Event.OnEmailChange ->
            state.update { it.copy(email = event.value, isEmailInvalid = false) }

        is AuthContract.Event.OnPasswordChange ->
            state.update { it.copy(password = event.value, isPasswordInvalid = false) }

        AuthContract.Event.OnPasswordVisibleToggle ->
            state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        is AuthContract.Event.OnUsernameChange ->
            state.update { it.copy(username = event.value, isUsernameInvalid = false) }

        AuthContract.Event.OnLogin -> onLogin()
        AuthContract.Event.OnSignUp -> onSignUp()
        is AuthContract.Event.OnAccountChange ->
            state.update { it.copy(account = event.value, isAccountInvalid = false) }
    }

    private fun onLogin() {
        viewModelScope.launch {
            when {
                state.value.account.isBlank() ->
                    state.update { it.copy(isAccountInvalid = true) }

                state.value.password.length < 8 ->
                    state.update { it.copy(isPasswordInvalid = true) }

                else -> {
                    runCatching {
                        val response = authApi.login(
                            request = LoginRequest(
                                account = state.value.account,
                                password = state.value.password
                            )
                        )
                        response.data?.let { updateToken(it) }
                    }.onFailure {
                        Log.e(TAG, "onLogin: ", it)
                    }
                }
            }
        }
    }

    private fun onSignUp() {
        viewModelScope.launch {
            when {
                !RegexUsernameStrict.matches(state.value.username) ->
                    state.update { it.copy(isUsernameInvalid = true) }

                !RegexEmail.matches(state.value.email) ->
                    state.update { it.copy(isEmailInvalid = true) }

                state.value.password.length < 8 ->
                    state.update { it.copy(isPasswordInvalid = true) }

                else -> {
                    runCatching {
                        val response = authApi.signup(
                            request = SignupRequest(
                                username = state.value.username,
                                email = state.value.email,
                                password = state.value.password
                            )
                        )
                        response.data?.let { updateToken(it) }
                    }.onFailure {
                        Log.e(TAG, "onSignUp: ", it)
                    }
                }
            }
        }
    }

    private suspend fun updateToken(token: String) {
        val jwt = JWT(token)
        val uid = jwt.getClaim(Claims.UID).asLong() ?: error("未解析出uid")
        Log.d(TAG, "uid: ${uid}, token: $token")
        dataStore.setToken(uid, token)
        dataStore.switchUser(uid)
    }
}
