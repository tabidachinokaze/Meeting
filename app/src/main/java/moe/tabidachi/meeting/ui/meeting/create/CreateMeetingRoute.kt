package moe.tabidachi.meeting.ui.meeting.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import moe.tabidachi.compose.mvi.observe
import moe.tabidachi.meeting.di.DateTimePickerDialog
import moe.tabidachi.meeting.di.DurationPickerDialog
import moe.tabidachi.meeting.ui.common.LocalSnackbarHostState
import moe.tabidachi.meeting.ui.meeting.created.MeetingCreatedRoute
import moe.tabidachi.meeting.ui.participants.select.SelectParticipantsRoute

@Serializable
data object CreateMeetingRoute : NavKey

@Composable
fun CreateMeetingRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: CreateMeetingViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val (state, event) = viewModel.observe {
        when (it) {
            is CreateMeetingContract.Effect.OnMeetingCreated -> {
                backStack.remove(CreateMeetingRoute)
                backStack.add(MeetingCreatedRoute(it.meeting))
            }

            is CreateMeetingContract.Effect.Toast -> {
                scope.launch { snackbarHostState.showSnackbar(it.text) }
            }
        }
    }
    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState
    ) {
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
                    },
                    onNameChange = {
                        event(CreateMeetingContract.Event.OnNameChange(it))
                    },
                    onDescriptionChange = {
                        event(CreateMeetingContract.Event.OnDescriptionChange(it))
                    },
                    onMeetingSchedule = {
                        event(CreateMeetingContract.Event.OnMeetingSchedule)
                    }
                )
            }
        )
    }
}
