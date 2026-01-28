package moe.tabidachi.meeting.ui.auth

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe

@Serializable
data object AuthRoute : NavKey

@Composable
fun AuthRoute(
    viewModel: AuthViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val (state, event) = viewModel.observe {
        when (it) {
            is AuthContract.Effect.Toast -> scope.launch { snackbarHostState.showSnackbar(message = it.text) }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { _ ->
        AuthScreen(
            state = state.value,
            actions = remember {
                AuthContract.Actions(
                    onAuthTypeChange = { event(AuthContract.Event.OnAuthTypeChange(it)) },
                    onUsernameChange = { event(AuthContract.Event.OnUsernameChange(it)) },
                    onPasswordChange = { event(AuthContract.Event.OnPasswordChange(it)) },
                    onEmailChange = { event(AuthContract.Event.OnEmailChange(it)) },
                    onAccountChange = { event(AuthContract.Event.OnAccountChange(it)) },
                    onPasswordVisibleToggle = { event(AuthContract.Event.OnPasswordVisibleToggle) },
                    onLogin = { event(AuthContract.Event.OnLogin) },
                    onSignUp = { event(AuthContract.Event.OnSignUp) },
                    onForgotPassword = {},
                )
            }
        )
    }
}
