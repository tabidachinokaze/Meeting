package moe.tabidachi.meeting.ui

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.SettingsEffect
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.ui.auth.AuthRoute
import moe.tabidachi.meeting.ui.common.DurationPickerDialog
import moe.tabidachi.meeting.ui.common.rememberDurationPickerState
import moe.tabidachi.meeting.ui.datetime.DateTimePickerScreen
import moe.tabidachi.meeting.ui.main.MainRoute
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingContract
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingKey
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingRoute
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingViewModel
import moe.tabidachi.meeting.ui.meeting.created.MeetingCreatedRoute
import moe.tabidachi.meeting.ui.participants.select.SelectParticipantsRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MeetingNavDisplay() {
    val settingsDataStore: SettingsDataStore = koinInject()
    val startDestination: StartDestination = koinInject()
    val backStack = rememberNavBackStack(startDestination.value)
    LaunchedEffect(Unit) {
        settingsDataStore.effect.collect {
            when (it) {
                is SettingsEffect.NavigateToAuthScreen -> {
                    backStack.clear()
                    backStack.add(AuthRoute)
                }
            }
        }
    }
    NavDisplay(
        backStack = backStack,
        sceneStrategies = listOf(DialogSceneStrategy(), SinglePaneSceneStrategy()),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<AuthRoute> {
                AuthRoute(
                    backStack = backStack,
                    viewModel = koinViewModel(),
                )
            }
            entry<MainRoute> {
                MainRoute(
                    backStack = backStack,
                    viewModel = koinViewModel(),
                )
            }
            entry<CreateMeetingKey.CreateMeetingNavDisplay> { route ->
                CreateMeetingNavDisplay(
                    onMeetingCreated = {
                        backStack.remove(route)
                        backStack.add(MeetingCreatedRoute(it))
                    },
                    onNavigateUp = {
                        backStack.remove(route)
                    }
                )
            }
            entry<MeetingCreatedRoute> { route ->
                MeetingCreatedRoute(
                    backStack = backStack,
                    viewModel = koinViewModel { parametersOf(route) }
                )
            }
        }
    )
}

@Composable
fun CreateMeetingNavDisplay(
    onMeetingCreated: (Meeting) -> Unit,
    onNavigateUp: () -> Unit
) {
    val backStack = rememberNavBackStack(CreateMeetingKey.CreateMeetingRoute)
    val viewModel: CreateMeetingViewModel = koinViewModel()
    NavDisplay(
        backStack = backStack,
        sceneStrategies = listOf(DialogSceneStrategy(), SinglePaneSceneStrategy()),
        entryProvider = entryProvider {
            entry<CreateMeetingKey.CreateMeetingRoute> {
                CreateMeetingRoute(
                    backStack = backStack,
                    viewModel = viewModel,
                    onMeetingCreated = onMeetingCreated,
                    onNavigateUp = onNavigateUp,
                )
            }
            entry<CreateMeetingKey.SelectParticipantsRoute> {
                SelectParticipantsRoute(
                    backStack = backStack,
                    viewModel = viewModel,
                )
            }
            entry<CreateMeetingKey.PickDateTimeRoute>(
                metadata = DialogSceneStrategy.dialog()
            ) {
                DateTimePickerScreen(
                    onNavigateUp = {
                        backStack.removeLastOrNull()
                    },
                    onDateTimePicked = {
                        viewModel.event(
                            event = CreateMeetingContract.Event.OnDateTimePicked(it)
                        )
                    },
                    initialDataTime = viewModel.state.collectAsState().value.selectedDateTime
                )
            }
            entry<CreateMeetingKey.PickDurationRoute>(
                metadata = DialogSceneStrategy.dialog()
            ) {
                val duration = viewModel.state.collectAsState().value.selectedDuration
                val durationPickerState = rememberDurationPickerState(duration)
                DurationPickerDialog(
                    state = durationPickerState,
                    onDismissRequest = { backStack.removeLastOrNull() },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.event(
                                    CreateMeetingContract.Event.OnDurationPicked(durationPickerState.duration)
                                )
                                backStack.removeLastOrNull()
                            }
                        ) {
                            Text(text = stringResource(R.string.duration_picker_dialog_confirm_button))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { backStack.removeLastOrNull() }
                        ) {
                            Text(text = stringResource(R.string.duration_picker_dialog_cancel_button))
                        }
                    },
                    visibleItemCount = 5
                )
            }
        }
    )
}

@JvmInline
value class StartDestination(val value: NavKey)