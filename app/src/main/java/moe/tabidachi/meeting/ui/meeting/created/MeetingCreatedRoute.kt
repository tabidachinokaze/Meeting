package moe.tabidachi.meeting.ui.meeting.created

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.model.Meeting

@Serializable
data class MeetingCreatedRoute(
    val meeting: Meeting
) : NavKey

@Composable
fun MeetingCreatedRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: MeetingCreatedViewModel
) {
    val (state, event) = viewModel.observe { }

    MeetingCreatedScreen(
        state = state.value,
        actions = MeetingCreatedContract.Actions(
            onNavigateUp = {
                backStack.removeLastOrNull()
            }
        )
    )
}
