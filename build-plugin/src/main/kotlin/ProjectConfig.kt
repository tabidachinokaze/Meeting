import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object ProjectConfig {
    object Android {
        const val COMPILE_SDK_VERSION: Int = 36
        const val MIN_SDK_VERSION: Int = 28
        const val TARGET_SDK_VERSION: Int = 36
    }

    object Compiler {
        val JAVA_COMPATIBILITY = JavaVersion.VERSION_1_8
        val JVM_TARGET = JvmTarget.JVM_1_8
    }
}
