package moe.tabidachi.meeting.ui.participants.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingContract
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingViewModel

@Composable
fun SelectParticipantsRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: CreateMeetingViewModel
) {
    val (state, event) = viewModel.observe { }
    LaunchedEffect(Unit) {
        viewModel.event(CreateMeetingContract.Event.FetchContacts)
    }
    SelectParticipantsScreen(
        state = state.value,
        actions = CreateMeetingContract.Actions(
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
            onParticipantAddOrRemove = {
                event(CreateMeetingContract.Event.OnParticipantAddOrRemove(it))
            },
            onParticipantsSelectCancel = {
                backStack.removeLastOrNull()
                event(CreateMeetingContract.Event.OnParticipantsSelectCancel)
            },
            onParticipantsSelectConfirm = {
                backStack.removeLastOrNull()
                event(CreateMeetingContract.Event.OnParticipantsSelectConfirm)
            },
            onContactQueryChange = {
                event(CreateMeetingContract.Event.OnContactQueryChange(it))
            }
        )
    )
}
