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
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.ui.common.LocalSnackbarHostState


@Serializable
sealed interface CreateMeetingKey : NavKey {
    @Serializable
    data object CreateMeetingNavDisplay : CreateMeetingKey

    @Serializable
    data object CreateMeetingRoute : CreateMeetingKey

    @Serializable
    data object SelectParticipantsRoute : CreateMeetingKey

    @Serializable
    data object PickDateTimeRoute : CreateMeetingKey

    @Serializable
    data object PickDurationRoute : CreateMeetingKey
}

@Composable
fun CreateMeetingRoute(
    backStack: NavBackStack<NavKey>,
    viewModel: CreateMeetingViewModel,
    onMeetingCreated: (Meeting) -> Unit,
    onNavigateUp: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val (state, event) = viewModel.observe {
        when (it) {
            is CreateMeetingContract.Effect.OnMeetingCreated -> onMeetingCreated(it.meeting)

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
            actions = CreateMeetingContract.Actions(
                onNavigateUp = onNavigateUp,
                onNavigateToSelectParticipants = {
                    event(CreateMeetingContract.Event.OnParticipantsSelectStart)
                    backStack.add(CreateMeetingKey.SelectParticipantsRoute)
                },
                onDateTimePick = {
                    backStack.add(CreateMeetingKey.PickDateTimeRoute)
                },
                onDurationPick = {
                    backStack.add(CreateMeetingKey.PickDurationRoute)
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
        )
    }
}
