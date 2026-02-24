package moe.tabidachi.meeting.ui.meeting.created

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.ui.preview.meetings

interface MeetingCreatedContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val meeting: Meeting,
        val meetingLink: String = "",
    ) {
        companion object {
            val Preview = State(
                meeting = meetings[0],
                meetingLink = "https://meeting.tabidachi.moe/join/2vg6igde6kq"
            )
        }
    }

    data class Actions(
        val onNavigateUp: () -> Unit = {},
    )

    sealed interface Event

    sealed interface Effect
}

class MeetingCreatedViewModel(
    private val route: MeetingCreatedRoute,
) : MeetingCreatedContract.ViewModel() {
    override val state: MutableStateFlow<MeetingCreatedContract.State>
        get() = MutableStateFlow(MeetingCreatedContract.State(meeting = route.meeting))
    override val effect: SharedFlow<MeetingCreatedContract.Effect>
        get() = MutableSharedFlow()

    override fun event(event: MeetingCreatedContract.Event) {
    }
}