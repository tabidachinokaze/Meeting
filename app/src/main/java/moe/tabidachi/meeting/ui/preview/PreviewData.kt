package moe.tabidachi.meeting.ui.preview

import moe.tabidachi.meeting.model.Meeting
import moe.tabidachi.meeting.model.MeetingStatus
import moe.tabidachi.meeting.model.UserInfo
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

val userInfoList = listOf(
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

val meetings = listOf(
    Meeting(
        id = 1,
        name = "Product Strategy Review",
        time = Clock.System.now(),
        duration = 45.minutes,
        participants = listOf(1, 2, 3),
        status = MeetingStatus.Upcoming,
        description = "",
        creatorId = 1,
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now()
    ),
    Meeting(
        id = 1,
        name = "Design Sprint Planning",
        time = Clock.System.now(),
        duration = 60.minutes,
        participants = listOf(4, 5),
        status = MeetingStatus.Upcoming,
        description = "",
        creatorId = 4,
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now()
    )
)