package moe.tabidachi.meeting.ui.meeting.create

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.model.UserInfo
import moe.tabidachi.meeting.ui.preview.userInfoList

interface CreateMeetingContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val name: String = "",
        val participants: List<UserInfo> = emptyList(),
        val contacts: List<UserInfo> = emptyList(),
        val selectedDateTime: LocalDateTime? = null,
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
        val onDateTimePick: () -> Unit = {},
        val onDurationPick: () -> Unit = {}
    )

    sealed interface Event {
        data class OnParticipantAddOrRemove(val participant: UserInfo) : Event
        data class OnDateTimePicked(val dateTime: LocalDateTime) : Event
    }

    sealed interface Effect
}

class CreateMeetingViewModel : CreateMeetingContract.ViewModel() {
    final override val state: StateFlow<CreateMeetingContract.State>
        field = MutableStateFlow(CreateMeetingContract.State.Preview)
    final override val effect: SharedFlow<CreateMeetingContract.Effect>
        field = MutableSharedFlow<CreateMeetingContract.Effect>()

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
    }
}