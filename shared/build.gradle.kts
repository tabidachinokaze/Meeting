plugins {
    id("tabidachi.library.kmp")
}

kotlin {
    androidLibrary {
        namespace = "moe.tabidachi.meeting.shared"
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}