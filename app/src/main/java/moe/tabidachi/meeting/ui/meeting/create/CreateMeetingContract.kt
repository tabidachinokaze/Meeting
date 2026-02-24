package moe.tabidachi.meeting.ui.meeting.create

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.R
import moe.tabidachi.meeting.data.api.MeetingApi
import moe.tabidachi.meeting.data.api.UserApi
import moe.tabidachi.meeting.model.CreateMeetingRequest
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.StatusCode
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.model.statusCode
import moe.tabidachi.meeting.ui.preview.userInfoList
import moe.tabidachi.meeting.utils.Debounce
import kotlin.time.Duration

interface CreateMeetingContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val name: String = "",
        @StringRes
        val nameErrorMessage: Int? = null,
        val description: String = "",
        val participants: List<UserInfo> = emptyList(),
        val contactsSorted: List<UserInfo> = emptyList(),
        val contacts: List<UserInfo> = emptyList(),
        val selectedDateTime: LocalDateTime? = null,
        @StringRes
        val dateTimeErrorMessage: Int? = null,
        val selectedDuration: Duration? = null,
        @StringRes
        val durationErrorMessage: Int? = null,
        val selectedParticipants: List<UserInfo> = emptyList(),
        val contactQuery: String = "",
        val isProcessing: Boolean = false
    ) {
        companion object {
            val Preview = State(
                name = "Product Strategy Review",
                participants = emptyList(),
                contacts = List(100) { index ->
                    userInfoList.random().copy(uid = index.toLong())
                }
            )
        }
    }

    data class Actions(
        val onNavigateUp: () -> Unit = {},
        val onMeetingSchedule: () -> Unit = {},
        val onNavigateToSelectParticipants: () -> Unit = {},
        val onParticipantAddOrRemove: (UserInfo) -> Unit = {},
        val onSelectedParticipantAddOrRemove: (UserInfo) -> Unit = {},
        val onDateTimePick: () -> Unit = {},
        val onDurationPick: () -> Unit = {},
        val onParticipantsSelectCancel: () -> Unit = {},
        val onParticipantsSelectConfirm: () -> Unit = {},
        val onContactQueryChange: (String) -> Unit = {},
        val onNameChange: (String) -> Unit = {},
        val onDescriptionChange: (String) -> Unit = {},
    )

    sealed interface Event {
        data class OnParticipantAddOrRemove(val participant: UserInfo) : Event
        data class OnSelectedParticipantAddOrRemove(val participant: UserInfo) : Event
        data class OnDateTimePicked(val dateTime: LocalDateTime) : Event
        data class OnDurationPicked(val duration: Duration) : Event
        data object OnParticipantsSelectStart : Event
        data object OnParticipantsSelectCancel : Event
        data object OnParticipantsSelectConfirm : Event
        data class OnContactQueryChange(val value: String) : Event
        data class OnNameChange(val value: String) : Event
        data class OnDescriptionChange(val value: String) : Event
        data object FetchContacts : Event
        data object OnMeetingSchedule : Event
    }

    sealed interface Effect {
        data class OnMeetingCreated(val meeting: Meeting) : Effect
        data class Toast(val text: String) : Effect
    }
}

class CreateMeetingViewModel(
    private val context: Context,
    private val userApi: UserApi,
    private val meetingApi: MeetingApi
) : CreateMeetingContract.ViewModel() {
    final override val state: StateFlow<CreateMeetingContract.State>
        field = MutableStateFlow(CreateMeetingContract.State())
    final override val effect: SharedFlow<CreateMeetingContract.Effect>
        field = MutableSharedFlow<CreateMeetingContract.Effect>()

    private val debounce = Debounce(
        coroutineScope = viewModelScope,
        isProcessing = { state.value.isProcessing },
        onProcessingStateChange = { isProcessing ->
            state.update { it.copy(isProcessing = isProcessing) }
        }
    )

    init {
        state.map { it.contacts }.distinctUntilChanged().flatMapLatest { contacts ->
            state.map { it.contactQuery }.distinctUntilChanged().debounce(500).onEach { query ->
                if (query.isBlank()) {
                    state.update { it.copy(contactsSorted = contacts) }
                } else {
                    state.update {
                        it.copy(
                            contactsSorted = FuzzySearch
                                .extractSorted(
                                    query = query,
                                    choices = contacts.map { it.username }
                                )
                                .map { contacts[it.index] }
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun event(event: CreateMeetingContract.Event) = when (event) {
        is CreateMeetingContract.Event.OnParticipantAddOrRemove -> state.update {
            if (event.participant in it.participants) {
                it.copy(participants = it.participants.filter { it != event.participant })
            } else {
                it.copy(participants = it.participants + event.participant)
            }
        }

        is CreateMeetingContract.Event.OnDateTimePicked -> state.update {
            it.copy(selectedDateTime = event.dateTime, dateTimeErrorMessage = null)
        }

        is CreateMeetingContract.Event.OnDurationPicked -> state.update {
            it.copy(selectedDuration = event.duration, durationErrorMessage = null)
        }

        CreateMeetingContract.Event.OnParticipantsSelectStart -> state.update {
            it.copy(participants = it.selectedParticipants)
        }

        CreateMeetingContract.Event.OnParticipantsSelectCancel -> state.update {
            it.copy(participants = emptyList(), contactQuery = "")
        }

        CreateMeetingContract.Event.OnParticipantsSelectConfirm -> state.update {
            it.copy(
                selectedParticipants = it.participants,
                participants = emptyList(),
                contactQuery = ""
            )
        }

        is CreateMeetingContract.Event.OnSelectedParticipantAddOrRemove -> state.update {
            if (event.participant in it.selectedParticipants) {
                it.copy(selectedParticipants = it.selectedParticipants.filter { it != event.participant })
            } else {
                it.copy(selectedParticipants = it.selectedParticipants + event.participant)
            }
        }

        is CreateMeetingContract.Event.OnContactQueryChange -> state.update {
            it.copy(contactQuery = event.value)
        }

        is CreateMeetingContract.Event.OnDescriptionChange -> state.update {
            it.copy(description = event.value)
        }

        is CreateMeetingContract.Event.OnNameChange -> state.update {
            it.copy(name = event.value, nameErrorMessage = null)
        }

        CreateMeetingContract.Event.FetchContacts -> fetchContacts()
        CreateMeetingContract.Event.OnMeetingSchedule -> onMeetingSchedule()
    }

    private fun onMeetingSchedule() = debounce.whenIdle {
        runCatching {
            val name = state.value.name.takeIf { it.isNotBlank() } ?: return@whenIdle state.update {
                it.copy(nameErrorMessage = R.string.create_meeting_screen_error_title_empty)
            }
            val selectedDateTime = state.value.selectedDateTime ?: return@whenIdle state.update {
                it.copy(dateTimeErrorMessage = R.string.create_meeting_screen_error_date_empty)
            }
            val selectedDuration = state.value.selectedDuration ?: return@whenIdle state.update {
                it.copy(durationErrorMessage = R.string.create_meeting_screen_error_duration_empty)
            }
            val response = meetingApi.createMeeting(
                request = CreateMeetingRequest(
                    name = name,
                    description = state.value.description,
                    time = selectedDateTime.toInstant(timeZone = TimeZone.currentSystemDefault()),
                    duration = selectedDuration,
                    participants = state.value.selectedParticipants.map { it.uid }
                )
            )
            when (response.statusCode) {
                StatusCode.Success -> {
                    response.data?.let { meeting ->
                        effect.emit(CreateMeetingContract.Effect.OnMeetingCreated(meeting))
                    }
                }

                StatusCode.InternalError -> effect.emit(
                    CreateMeetingContract.Effect.Toast(context.getString(R.string.error_server_internal))
                )

                else -> effect.emit(CreateMeetingContract.Effect.Toast(context.getString(R.string.create_meeting_screen_error_create_failed_toast)))
            }
        }.onFailure {
            it.printStackTrace()
            effect.emit(
                CreateMeetingContract.Effect.Toast(
                    it.message
                        ?: context.getString(R.string.create_meeting_screen_error_create_failed_toast)
                )
            )
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            runCatching {
                val response = userApi.getContracts()
                when (response.statusCode) {
                    StatusCode.Success -> state.update {
                        it.copy(contacts = response.data)
                    }

                    else -> Unit
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}