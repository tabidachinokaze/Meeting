import moe.tabidachi.gradle.plugin.libs

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdk = ProjectConfig.Android.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ProjectConfig.Android.MIN_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY
        targetCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(ProjectConfig.Compiler.JVM_TARGET)
        freeCompilerArgs.addAll(
            "-Xexplicit-backing-fields"
        )
    }
}

dependencies {

}
