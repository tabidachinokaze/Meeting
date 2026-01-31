package moe.tabidachi.meeting.ui.meeting.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.di.DateTimePickerDialog
import moe.tabidachi.meeting.di.DurationPickerDialog
import moe.tabidachi.meeting.ui.participants.select.SelectParticipantsRoute

@Serializable
data object CreateMeetingRoute : NavKey

@Composable
fun CreateMeetingRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: CreateMeetingViewModel
) {
    val (state, event) = viewModel.observe { }
    CreateMeetingScreen(
        state = state.value,
        actions = remember {
            CreateMeetingContract.Actions(
                onNavigateUp = {
                    backStack.removeLastOrNull()
                },
                onNavigateToSelectParticipants = {
                    event(CreateMeetingContract.Event.OnParticipantsSelectStart)
                    backStack.add(SelectParticipantsRoute)
                },
                onDateTimePick = {
                    backStack.add(DateTimePickerDialog)
                },
                onDurationPick = {
                    backStack.add(DurationPickerDialog)
                },
                onSelectedParticipantAddOrRemove = {
                    event(CreateMeetingContract.Event.OnSelectedParticipantAddOrRemove(it))
                }
            )
        }
    )
}
