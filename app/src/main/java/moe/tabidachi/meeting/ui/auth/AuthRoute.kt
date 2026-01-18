package moe.tabidachi.meeting.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
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
    val (state, event) = viewModel.observe {
        when (it) {
            is AuthContract.Effect -> Unit
        }
    }
    AuthScreen(
        state = state.value,
        actions = remember {
            AuthContract.Actions(
                onAuthTypeChange = { event(AuthContract.Event.OnAuthTypeChange(it)) },
                onUsernameChange = { event(AuthContract.Event.OnUsernameChange(it)) },
                onPasswordChange = { event(AuthContract.Event.OnPasswordChange(it)) },
                onEmailChange = { event(AuthContract.Event.OnEmailChange(it)) },
                onPasswordVisibleToggle = { event(AuthContract.Event.OnPasswordVisibleToggle) },
            )
        }
    )
}

@OptIn(KoinExperimentalAPI::class)
fun Module.auth() {
    viewModel {
        AuthViewModel(context = get())
    }
    navigation<AuthRoute> {
        AuthRoute(viewModel = koinViewModel())
    }
}