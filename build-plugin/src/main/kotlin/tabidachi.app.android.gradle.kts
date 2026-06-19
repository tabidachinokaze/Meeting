import moe.tabidachi.gradle.plugin.libs

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdk = ProjectConfig.Android.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ProjectConfig.Android.MIN_SDK_VERSION
        targetSdk = ProjectConfig.Android.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY
        targetCompatibility = ProjectConfig.Compiler.JAVA_COMPATIBILITY
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(ProjectConfig.Compiler.JVM_TARGET)
    }
}

dependencies {

}
