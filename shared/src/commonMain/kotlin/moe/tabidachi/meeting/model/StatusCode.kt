package moe.tabidachi.meeting.model

sealed class StatusCode(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val message: String
) {
    val code: Int get() = major.shl(8 * 2) or minor.shl(8 * 1) or patch

    object LoginSuccess : StatusCode(0, 1, 0, "登录成功")
    object LoginFailure : StatusCode(0, 1, 1, "登录失败")
    object UserNotRegistered : StatusCode(0, 1, 2, "用户未注册")
    object SignUpSuccess : StatusCode(0, 1, 3, "注册成功")
    object SignUpFailure : StatusCode(0, 1, 4, "注册失败")

    // 用户相关
    object EmailAlreadyExists : StatusCode(4, 0, 0, "邮箱已注册")
    object UsernameAlreadyExists : StatusCode(4, 0, 1, "用户名已被占用")
    object PasswordTooWeak : StatusCode(4, 0, 2, "密码长度必须至少为8个字符")
    object PasswordMismatch : StatusCode(4, 0, 3, "密码不匹配")

    // 验证错误
    object InvalidEmail : StatusCode(4, 1, 0, "Invalid email format")
    object InvalidPhone : StatusCode(4, 1, 1, "Invalid phone number")
    object InvalidUsername : StatusCode(4, 1, 2, "Invalid username format")

    // 系统错误
    object InternalError : StatusCode(4, 2, 2, message = "Internal server error")

}

inline fun <reified T> StatusCode.withData(data: T): Response<T> =
    Response(code = code, message = message, data = data)

fun StatusCode.emptyData(): Response<String?> =
    Response(code = code, message = message, data = null)
