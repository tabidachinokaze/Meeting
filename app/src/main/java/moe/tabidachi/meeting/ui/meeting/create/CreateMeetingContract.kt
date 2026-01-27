package moe.tabidachi.meeting.ui.meeting.create

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import moe.tabidachi.compose.mvi.BackingFieldsViewModel
import moe.tabidachi.meeting.model.UserInfo

interface CreateMeetingContract {
    abstract class ViewModel : BackingFieldsViewModel<State, Event, Effect>()

    data class State(
        val name: String = "",
        val participants: List<UserInfo> = emptyList()
    ) {
        companion object {
            val Preview = State(
                name = "Product Strategy Review",
                participants = listOf(
                    UserInfo(
                        uid = 1,
                        username = "Sarah Kim",
                        email = "sarah.k@company.com",
                    ),
                    UserInfo(
                        uid = 2,
                        username = "Mike Thompson",
                        email = "mike.t@company.com",
                    ),
                    UserInfo(
                        uid = 3,
                        username = "Emily Roberts",
                        email = "emily.r@company.com",
                    ),
                    UserInfo(
                        uid = 4,
                        username = "Alex Parker",
                        email = "alex.p@company.com",
                    ),
                    UserInfo(
                        uid = 5,
                        username = "John Davis",
                        email = "john.d@company.com",
                    ),
                    UserInfo(
                        uid = 6,
                        username = "Lisa Anderson",
                        email = "lisa.a@company.com",
                    ),
                    UserInfo(
                        uid = 7,
                        username = "David Wilson",
                        email = "david.w@company.com",
                    ),
                    UserInfo(
                        uid = 8,
                        username = "Jennifer Lee",
                        email = "jennifer.l@company.com",
                    ),
                )
            )
        }
    }

    data class Actions(
        val onClick: () -> Unit = {},
    )

    sealed interface Event

    sealed interface Effect
}

class CreateMeetingViewModel : CreateMeetingContract.ViewModel() {
    final override val state: StateFlow<CreateMeetingContract.State>
        field = MutableStateFlow(CreateMeetingContract.State.Preview)
    final override val effect: SharedFlow<CreateMeetingContract.Effect>
        field = MutableSharedFlow<CreateMeetingContract.Effect>()

    override fun event(event: CreateMeetingContract.Event) {
    }
}