package moe.tabidachi.meeting.routing

import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import moe.tabidachi.meeting.ktx.requireUserId
import moe.tabidachi.meeting.service.UserService

fun Route.contact() {
    val userService: UserService by application.dependencies

    get("/contacts") {
        val userId = requireUserId()
        call.respond(userService.getContracts(userId))
    }
}