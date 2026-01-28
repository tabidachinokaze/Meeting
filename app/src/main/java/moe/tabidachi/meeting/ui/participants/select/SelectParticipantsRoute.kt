package moe.tabidachi.meeting.ui.participants.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingContract
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingViewModel

@Serializable
data object SelectParticipantsRoute : NavKey

@Composable
fun SelectParticipantsRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: CreateMeetingViewModel
) {
    val (state, event) = viewModel.observe { }
    SelectParticipantsScreen(
        state = state.value,
        actions = remember {
            CreateMeetingContract.Actions(
                onNavigateUp = {
                    backStack.removeLastOrNull()
                },
                onParticipantAddOrRemove = {
                    event(CreateMeetingContract.Event.OnParticipantAddOrRemove(it))
                }
            )
        }
    )
}
