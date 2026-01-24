package moe.tabidachi.meeting.ui.main

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import moe.tabidachi.compose.mvi.BackingFieldsViewModel

interface MainContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val currentTab: MainTab = MainTab.Home,
        val username: String = "",
        val email: String = "",
        val isNotificationEnabled: Boolean = false,
        val isDarkModeEnabled: Boolean = false,
        val isAutoRecordEnabled: Boolean = false,
        val monthMeetingCount: Int = 0,
        val todolistCount: Int = 0,
        val meetingHours: Double = 0.0
    ) {
        companion object {
            val Preview = State(
                currentTab = MainTab.Settings,
                username = "kaze",
                email = "kaze@tabidachi.moe"
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
    }

    sealed interface Effect
}

class MainViewModel : MainContract.ViewModel() {
    final override val state: StateFlow<MainContract.State>
        field = MutableStateFlow(
            MainContract.State(
                currentTab = MainTab.Settings,
                username = "",
                email = ""
            )
        )

    final override val effect: SharedFlow<MainContract.Effect>
        field = MutableSharedFlow()

    override fun event(event: MainContract.Event) = when (event) {
        is MainContract.Event.OnTabClick -> state.update { it.copy(currentTab = event.page) }
    }
}