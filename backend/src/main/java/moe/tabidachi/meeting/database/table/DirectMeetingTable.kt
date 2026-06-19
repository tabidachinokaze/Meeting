package moe.tabidachi.meeting.database.table

import org.jetbrains.exposed.v1.core.Table

object DirectMeetingTable : Table("direct_meeting") {
    val meetingId = long("meeting_id").references(MeetingTable.id)
    val token = text("token")

    override val primaryKey: PrimaryKey = PrimaryKey(token)
}