package moe.tabidachi.meeting.ui.meeting.create

import androidx.lifecycle.viewModelScope
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.ui.preview.userInfoList
import kotlin.time.Duration

interface CreateMeetingContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val name: String = "",
        val participants: List<UserInfo> = emptyList(),
        val contactsSorted: List<UserInfo> = emptyList(),
        val contacts: List<UserInfo> = emptyList(),
        val selectedDateTime: LocalDateTime? = null,
        val selectedDuration: Duration? = null,
        val selectedParticipants: List<UserInfo> = emptyList(),
        val contactQuery: String = ""
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
        val onScheduleMeeting: () -> Unit = {},
        val onNavigateToSelectParticipants: () -> Unit = {},
        val onParticipantAddOrRemove: (UserInfo) -> Unit = {},
        val onSelectedParticipantAddOrRemove: (UserInfo) -> Unit = {},
        val onDateTimePick: () -> Unit = {},
        val onDurationPick: () -> Unit = {},
        val onParticipantsSelectCancel: () -> Unit = {},
        val onParticipantsSelectConfirm: () -> Unit = {},
        val onContactQueryChange: (String) -> Unit = {},
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
    }

    sealed interface Effect
}

class CreateMeetingViewModel : CreateMeetingContract.ViewModel() {
    final override val state: StateFlow<CreateMeetingContract.State>
        field = MutableStateFlow(CreateMeetingContract.State.Preview)
    final override val effect: SharedFlow<CreateMeetingContract.Effect>
        field = MutableSharedFlow<CreateMeetingContract.Effect>()

    init {
        state.map { it.contactQuery }.distinctUntilChanged().debounce(500).onEach { query ->
            if (query.isBlank()) {
                state.update { it.copy(contactsSorted = it.contacts) }
            } else {
                state.update { state ->
                    state.copy(
                        contactsSorted = FuzzySearch.extractSorted(
                            query,
                            state.contacts.map { it.username }).map {
                            state.contacts[it.index]
                        }
                    )
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
            it.copy(selectedDateTime = event.dateTime)
        }

        is CreateMeetingContract.Event.OnDurationPicked -> state.update {
            it.copy(selectedDuration = event.duration)
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
    }
}