package moe.tabidachi.meeting.ktx

import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingContext
import moe.tabidachi.meeting.jwt.Claims

fun RoutingContext.requireUserId(): Long {
    return call.principal<JWTPrincipal>()?.getClaim(Claims.UID, Long::class) ?: error("require user id")
}