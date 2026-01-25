package moe.tabidachi.meeting.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.UserApi
import moe.tabidachi.meeting.ktx.TAG
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.model.statusCode

interface MainContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val currentTab: MainTab = MainTab.Home,
        val isNotificationEnabled: Boolean = false,
        val isDarkModeEnabled: Boolean = false,
        val isAutoRecordEnabled: Boolean = false,
        val monthMeetingCount: Int = 0,
        val todolistCount: Int = 0,
        val meetingHours: Double = 0.0,
        val userInfo: UserInfo? = null
    ) {
        companion object {
            val Preview = State(
                currentTab = MainTab.Settings,
                userInfo = UserInfo.Empty.copy(
                    username = "kaze",
                    email = "kaze@tabidachi.moe",
                )
            )
        }
    }

    data class Actions(
        val onTabClick: (MainTab) -> Unit = {},
        val onNotificationChange: (Boolean) -> Unit = {},
        val onDarkModeChange: (Boolean) -> Unit = {},
        val onAutoRecordChange: (Boolean) -> Unit = {},
        val onEditProfileClick: () -> Unit = {},
        val onEmailClick: () -> Unit = {},
        val onPrivacyClick: () -> Unit = {},
        val onVideoSettingsClick: () -> Unit = {},
        val onTranscriptClick: () -> Unit = {},
        val onCalendarClick: () -> Unit = {},
        val onLanguageClick: () -> Unit = {},
        val onHelpClick: () -> Unit = {},
        val onLogout: () -> Unit = {}
    )

    sealed interface Event {
        data class OnTabClick(val page: MainTab) : Event
        data object OnLogout : Event
    }

    sealed interface Effect
}

class MainViewModel(
    private val userApi: UserApi,
    private val dataStore: SettingsDataStore
) : MainContract.ViewModel() {
    final override val state: StateFlow<MainContract.State>
        field = MutableStateFlow(
            MainContract.State(
                currentTab = MainTab.Settings,
            )
        )

    final override val effect: SharedFlow<MainContract.Effect>
        field = MutableSharedFlow()

    init {
        fetchUserInfo()
    }

    override fun event(event: MainContract.Event) = when (event) {
        is MainContract.Event.OnTabClick -> state.update { it.copy(currentTab = event.page) }
        MainContract.Event.OnLogout -> dataStore.logout()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            runCatching {
                val response = userApi
                    .getUserInfo(uid = dataStore.settings.value.uid)
                when (response.statusCode) {
                    StatusCode.Success -> {
                        response.data?.let { userInfo ->
                            state.update { it.copy(userInfo = userInfo) }
                        }
                    }

                    else -> Unit
                }
            }.onFailure {
                Log.e(TAG, "fetchUserInfo: ", it)
            }
        }
    }
}