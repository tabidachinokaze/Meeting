package moe.tabidachi.meeting.routing

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.tabidachi.meeting.jwt.Claims
import moe.tabidachi.meeting.service.UserService

fun Route.user() {
    val userService: UserService by application.dependencies

    get("/users/{uid}") {
        val uid = call.parameters["uid"]!!.toLong()
        val principal = call.principal<JWTPrincipal>()
        val from = principal?.getClaim(Claims.UID, Long::class)
        call.respond(userService.getUserInfo(uid, from == uid))
    }
}