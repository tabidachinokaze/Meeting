package moe.tabidachi.meeting.ui.auth

import android.content.Context
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.R

interface AuthContract {
    abstract class ViewModel() : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val authType: AuthType = AuthType.Login,
        val username: String = "",
        val password: String = "",
        val email: String = "",
        val isPasswordVisible: Boolean = false
    )

    data class Actions(
        val onAuthTypeChange: (AuthType) -> Unit = {},
        val onUsernameChange: (String) -> Unit = {},
        val onPasswordChange: (String) -> Unit = {},
        val onEmailChange: (String) -> Unit = {},
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
        data object OnPasswordVisibleToggle : Event
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
    private val context: Context
) : AuthContract.ViewModel() {
    final override val state: StateFlow<AuthContract.State>
        field = MutableStateFlow(AuthContract.State())
    final override val effect: SharedFlow<AuthContract.Effect>
        field = MutableSharedFlow<AuthContract.Effect>()

    override fun event(event: AuthContract.Event) = when (event) {
        is AuthContract.Event.OnAuthTypeChange -> state.update { it.copy(authType = event.authType) }
        is AuthContract.Event.OnEmailChange -> state.update { it.copy(email = event.value) }
        is AuthContract.Event.OnPasswordChange -> state.update { it.copy(password = event.value) }
        AuthContract.Event.OnPasswordVisibleToggle -> state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        is AuthContract.Event.OnUsernameChange -> state.update { it.copy(username = event.value) }
    }
}
