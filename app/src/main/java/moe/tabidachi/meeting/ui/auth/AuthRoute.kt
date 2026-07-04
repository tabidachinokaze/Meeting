package moe.tabidachi.meeting.ui.auth

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.ui.main.MainRoute

@Serializable
data object AuthRoute : NavKey

@Composable
fun AuthRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: AuthViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val (state, event) = viewModel.observe {
        when (it) {
            is AuthContract.Effect.Toast -> scope.launch { snackbarHostState.showSnackbar(message = it.text) }
            AuthContract.Effect.NavigateToMainScreen -> {
                backStack.clear()
                backStack.add(MainRoute)
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.imePadding())
        }
    ) { _ ->
        AuthScreen(
            state = state.value,
            actions = AuthContract.Actions(
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
        )
    }
}
