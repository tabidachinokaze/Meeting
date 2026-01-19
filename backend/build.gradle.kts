import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    // server
    implementation(libs.bundles.ktor.server)
    // exposed
    implementation(libs.bundles.exposed)
    // database
    implementation(libs.postgresql)
    implementation(libs.h2)
    // log
    implementation(libs.logback.classic)
    // other
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.s3)
    implementation(libs.argon2.jvm)

    implementation(project(":shared"))
}
