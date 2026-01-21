pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repo.clojars.org/")
    }
}

rootProject.name = "Meeting"

when (System.getProperty("idea.vendor.name")) {
    "Google" -> {
        include(":app")
        include(":compose-mvi")
    }

    "JetBrains" -> {
        include(":backend")
    }
}
include(":shared")
