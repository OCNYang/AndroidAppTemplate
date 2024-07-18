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
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }
}

rootProject.name = "AndroidAppTemplate"
include(":app")
include(":base")
include(":network")
project(":network").projectDir = File(settingsDir, "./network/network")
include(":api")
project(":api").projectDir = File(settingsDir, "./network/api")
include(":webview-x5")
project(":webview-x5").projectDir = File(settingsDir, "./webview/webview-x5")
include(":glance")
include(":aop")
include(":network-monitor")
project(":network-monitor").projectDir = File(settingsDir, "./network/network-monitor")
