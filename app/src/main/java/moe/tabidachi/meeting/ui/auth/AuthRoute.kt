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
import org.koin.androidx.scope.dsl.activityScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.navigation3.navigation

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

@OptIn(KoinExperimentalAPI::class)
fun Module.auth() {
    viewModel {
        AuthViewModel(
            context = get(),
            authApi = get(),
            dataStore = get(),
            backStack = get()
        )
    }
    navigation<AuthRoute> {
        AuthRoute(viewModel = koinViewModel())
    }
}