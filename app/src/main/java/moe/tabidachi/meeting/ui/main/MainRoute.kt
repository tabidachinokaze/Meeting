package moe.tabidachi.meeting.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingRoute
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.navigation3.navigation

@Serializable
data object MainRoute : NavKey

@Composable
fun MainRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: MainViewModel
) {
    val (state, event) = viewModel.observe { }
    MainScreen(
        state = state.value,
        actions = remember {
            MainContract.Actions(
                onTabClick = { event(MainContract.Event.OnTabClick(it)) },
                onLogout = { event(MainContract.Event.OnLogout) },
                onScheduleMeetingClick = {
                    backStack.add(CreateMeetingRoute)
                }
            )
        }
    )
}

@OptIn(KoinExperimentalAPI::class)
fun Module.main() {
    viewModel {
        MainViewModel(
            userApi = get(),
            dataStore = get()
        )
    }
    navigation<MainRoute> {
        MainRoute(
            backStack = get(),
            viewModel = get(),
        )
    }
}