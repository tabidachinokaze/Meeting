package moe.tabidachi.meeting.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingRoute

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
