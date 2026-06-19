import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureKotlinJavaCompatibility() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(ProjectConfig.Compiler.JVM_TARGET)
        }
    }
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY.toString()
        targetCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY.toString()
    }
}
