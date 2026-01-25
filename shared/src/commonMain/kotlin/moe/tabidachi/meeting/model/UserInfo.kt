package moe.tabidachi.meeting.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class UserInfo(
    val uid: Long,
    val username: String,
    val email: String?,
    val phone: String?,
    val avatar: String?,
    val createTime: Instant?,
    val updateTime: Instant?,
) {
    companion object {
        val Empty = UserInfo(
            uid = 0,
            username = "",
            email = "",
            phone = null,
            avatar = null,
            createTime = null,
            updateTime = null
        )
    }
}