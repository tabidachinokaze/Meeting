package moe.tabidachi.meeting.di

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.AuthApi
import moe.tabidachi.meeting.data.api.MeetingApi
import moe.tabidachi.meeting.data.api.UserApi
import moe.tabidachi.meeting.data.model.Settings
import moe.tabidachi.meeting.ktx.dataStore
import moe.tabidachi.meeting.shared.SharedHttpClient
import moe.tabidachi.meeting.shared.SharedJson
import moe.tabidachi.meeting.ui.auth.AuthRoute
import moe.tabidachi.meeting.ui.auth.AuthViewModel
import moe.tabidachi.meeting.ui.common.DurationPickerDialog
import moe.tabidachi.meeting.ui.common.rememberDurationPickerState
import moe.tabidachi.meeting.ui.datetime.DateTimePickerScreen
import moe.tabidachi.meeting.ui.main.MainRoute
import moe.tabidachi.meeting.ui.main.MainViewModel
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingContract
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingRoute
import moe.tabidachi.meeting.ui.meeting.create.CreateMeetingViewModel
import moe.tabidachi.meeting.ui.participants.select.SelectParticipantsRoute
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
val routeModule = module {
    activityRetainedScope {
        scoped<NavBackStack<NavKey>> {
            val settingsDataStore: SettingsDataStore = get()
            val startDestination: NavKey = when (settingsDataStore.token) {
                null -> AuthRoute
                else -> MainRoute
            }
            val backStack: NavBackStack<NavKey> = NavBackStack(startDestination)
            settingsDataStore.setBackStack(backStack)
            backStack
        }
        scoped {
            CreateMeetingViewModel(
                context = get(),
                userApi = get(),
                meetingApi = get(),
            )
        }
        viewModel {
            AuthViewModel(
                context = get(),
                authApi = get(),
                dataStore = get(),
                backStack = get()
            )
        }
        navigation<AuthRoute> {
            AuthRoute(viewModel = get())
        }
        scoped {
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
        navigation<CreateMeetingRoute> {
            CreateMeetingRoute(
                backStack = get(),
                viewModel = get(),
            )
        }
        navigation<SelectParticipantsRoute> {
            SelectParticipantsRoute(
                backStack = get(),
                viewModel = get()
            )
        }
        navigation<DateTimePickerDialog>(
            metadata = DialogSceneStrategy.dialog()
        ) {
            val backStack: NavBackStack<NavKey> = get()
            val viewModel: CreateMeetingViewModel = get()
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
        navigation<DurationPickerDialog>(
            metadata = DialogSceneStrategy.dialog()
        ) {
            val backStack: NavBackStack<NavKey> = get()
            val viewModel: CreateMeetingViewModel = get()
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
}

@Serializable
data object DateTimePickerDialog : NavKey

@Serializable
data object DurationPickerDialog : NavKey

val appModule = module {
    single<DataStore<Settings>> {
        val context: Context = get()
        context.dataStore
    }
    single {
        SettingsDataStore(dataStore = get())
    }
    single<Json> {
        SharedJson()
    }
    single<HttpClient> {
        val dataStore: SettingsDataStore = get()
        SharedHttpClient(
            json = get(),
            tokenProvider = { dataStore.token }
        )
    }
    single<AuthApi> {
        val dataStore: SettingsDataStore = get()
        AuthApi(
            client = get(),
            baseUrl = { dataStore.settings.value.baseUrl }
        )
    }
    single<UserApi> {
        val dataStore: SettingsDataStore = get()
        UserApi(
            client = get(),
            baseUrl = { dataStore.settings.value.baseUrl }
        )
    }
    single<MeetingApi> {
        val dataStore: SettingsDataStore = get()
        MeetingApi(
            client = get(),
            baseUrl = { dataStore.settings.value.baseUrl },
        )
    }
}