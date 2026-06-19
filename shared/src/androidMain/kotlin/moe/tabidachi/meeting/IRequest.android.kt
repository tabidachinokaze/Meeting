package moe.tabidachi.meeting

interface AndroidRequest {
    fun getType()
}

actual typealias IRequest = AndroidRequest
