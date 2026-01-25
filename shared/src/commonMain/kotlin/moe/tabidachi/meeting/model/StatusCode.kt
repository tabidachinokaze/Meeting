package moe.tabidachi.meeting.model

enum class StatusCode(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val message: String
) {
    LoginSuccess(0, 1, 0, "登录成功"),
    LoginFailure(0, 1, 1, "登录失败"),
    UserNotRegistered(0, 1, 2, "用户未注册"),
    SignUpSuccess(0, 1, 3, "注册成功"),
    SignUpFailure(0, 1, 4, "注册失败"),
    Success(0, 1, 5, "成功"),

    // 用户相关
    EmailAlreadyExists(4, 0, 0, "邮箱已注册"),
    UsernameAlreadyExists(4, 0, 1, "用户名已被占用"),
    PasswordTooWeak(4, 0, 2, "密码长度必须至少为8个字符"),
    PasswordMismatch(4, 0, 3, "密码不匹配"),
    UserNotFound(4, 0, 4, "用户不存在"),

    // 验证错误
    InvalidEmail(4, 1, 0, "Invalid email format"),
    InvalidPhone(4, 1, 1, "Invalid phone number"),
    InvalidUsername(4, 1, 2, "Invalid username format"),

    // 系统错误
    InternalError(4, 2, 0, message = "Internal server error");

    val code: Int = major.shl(8 * 2) or minor.shl(8 * 1) or patch

    companion object {
        fun valueOf(code: Int): StatusCode? {
            return entries.find { it.code == code }
        }
    }
}

inline fun <reified T> StatusCode.withData(data: T): Response<T> =
    Response(code = code, message = message, data = data)

fun StatusCode.emptyData(): Response<String?> =
    Response(code = code, message = message, data = null)

val Response<*>.statusCode: StatusCode? get() = StatusCode.valueOf(code)
