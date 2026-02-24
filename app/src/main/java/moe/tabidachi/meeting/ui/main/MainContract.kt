package moe.tabidachi.meeting.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.SettingsDataStore
import moe.tabidachi.meeting.data.api.MeetingApi
import moe.tabidachi.meeting.data.api.UserApi
import moe.tabidachi.meeting.domain.model.MeetingItem
import moe.tabidachi.meeting.ktx.TAG
import moe.tabidachi.meeting.model.MeetingStatus
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.model.statusCode
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

interface MainContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val currentTab: MainTab = MainTab.Home,
        // settings page
        val isNotificationEnabled: Boolean = false,
        val isDarkModeEnabled: Boolean = false,
        val isAutoRecordEnabled: Boolean = false,
        val monthMeetingCount: Int = 0,
        val todolistCount: Int = 0,
        val meetingHours: Double = 0.0,
        val userInfo: UserInfo? = null,
        // home page
        val greeting: String = "",
        val tips: String? = null,
        val meetings: List<MeetingItem> = emptyList()
    ) {
        companion object {
            val Preview = State(
                currentTab = MainTab.Settings,
                userInfo = UserInfo(
                    uid = 1,
                    username = "kaze",
                    email = "kaze@tabidachi.moe",
                ),
                greeting = "Good Morning, kaze",
                tips = "You have 3 high-priority tasks due today. Would you like to reschedule your 2:30 PM meeting?",
                meetings = listOf(
                    MeetingItem(
                        id = 1,
                        name = "Product Strategy Review",
                        time = Clock.System.now(),
                        duration = 45.minutes,
                        participants = listOf(
                            UserInfo(
                                uid = 1,
                                username = "Sarah K."
                            ),
                            UserInfo(
                                uid = 2,
                                username = "Mike T."
                            ),
                            UserInfo(
                                uid = 3,
                                username = "Alex P."
                            )
                        ),
                        status = MeetingStatus.Upcoming
                    ),
                    MeetingItem(
                        id = 1,
                        name = "Design Sprint Planning",
                        time = Clock.System.now(),
                        duration = 60.minutes,
                        participants = listOf(
                            UserInfo(
                                uid = 4,
                                username = "Emily R."
                            ),
                            UserInfo(
                                uid = 5,
                                username = "John D."
                            )
                        ),
                        status = MeetingStatus.Upcoming
                    )
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
        val onLogout: () -> Unit = {},
        val onScheduleMeetingClick: () -> Unit = {},
        val onJoinNowClick: () -> Unit = {},
    )

    sealed interface Event {
        data class OnTabClick(val page: MainTab) : Event
        data object OnLogout : Event
    }

    sealed interface Effect
}

class MainViewModel(
    private val context: Context,
    private val userApi: UserApi,
    private val dataStore: SettingsDataStore,
    private val meetingApi: MeetingApi
) : MainContract.ViewModel() {
    final override val state: StateFlow<MainContract.State>
        field = MutableStateFlow(MainContract.State())

    final override val effect: SharedFlow<MainContract.Effect>
        field = MutableSharedFlow()

    init {
        fetchUserInfo()
        fetchMeetings()
        state.map { it.userInfo }.distinctUntilChanged().onEach { userInfo ->
            if (userInfo == null) {
                state.update { it.copy(greeting = "") }
            } else {
                state.update {
                    it.copy(
                        greeting = context.getString(
                            R.string.home_page_greeting_format,
                            userInfo.username
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun event(event: MainContract.Event) = when (event) {
        is MainContract.Event.OnTabClick -> state.update { it.copy(currentTab = event.page) }
        MainContract.Event.OnLogout -> dataStore.logout()
    }

    private fun fetchMeetings() {
        viewModelScope.launch {
            runCatching {
                val response = meetingApi.getMeetings()
                when (response.statusCode) {
                    StatusCode.Success -> {
                        response.data?.let { meetings ->
                            state.update {
                                it.copy(
                                    meetings = meetings.map {
                                        MeetingItem(
                                            id = it.id,
                                            name = it.name,
                                            time = it.time,
                                            duration = it.duration,
                                            participants = it.participants.mapNotNull {
                                                userApi.getUserInfo(it).data
                                            },
                                            status = it.status,
                                        )
                                    }
                                )
                            }
                        }
                    }

                    StatusCode.InternalError -> {

                    }

                    else -> Unit
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
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