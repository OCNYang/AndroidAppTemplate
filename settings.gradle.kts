pluginManagement {
    repositories {
//        阿里镜像，有时无法获取到最新版本的库
//        maven("https://maven.aliyun.com/repository/google/")
//        maven("https://maven.aliyun.com/repository/gradle-plugin/")
//        maven("https://maven.aliyun.com/repository/public/")
//        maven("https://maven.aliyun.com/repository/jcenter/")
//        maven("https://maven.aliyun.com/repository/central/")
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
//        maven("https://maven.aliyun.com/repository/google/")
//        maven("https://maven.aliyun.com/repository/gradle-plugin/")
//        maven("https://maven.aliyun.com/repository/public/")
//        maven("https://maven.aliyun.com/repository/jcenter/")
//        maven("https://maven.aliyun.com/repository/central/")
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }
}

rootProject.name = "AndroidAppTemplate"
include(":app")
include(":base")

// library
include(":library_network")
project(":library_network").projectDir = File(settingsDir, "./library/network/library_network")
include(":library_network_monitor")
project(":library_network_monitor").projectDir = File(settingsDir, "./library/network/library_network_monitor")
include(":library_webview")
project(":library_webview").projectDir = File(settingsDir, "./library/library_webview")
include(":library_glance")
project(":library_glance").projectDir = File(settingsDir, "./library/library_glance")
include(":library_aop")
project(":library_aop").projectDir = File(settingsDir, "./library/library_aop")
include(":library_image_load")
project(":library_image_load").projectDir = File(settingsDir, "./library/library_image_load")
include(":library_ui_uniform")
project(":library_ui_uniform").projectDir = File(settingsDir, "./library/library_ui_uniform")

// api
include(":api_base")
project(":api_base").projectDir = File(settingsDir, "./api/api_base")
include(":api_main")
project(":api_main").projectDir = File(settingsDir, "./api/api_main")

// api_imp
include(":api_main_imp")
project(":api_main_imp").projectDir = File(settingsDir, "./api_imp/api_main_imp")


// module
include(":module_main")
project(":module_main").projectDir = File(settingsDir, "./module/module_main")
include(":module_dev_tools")
project(":module_dev_tools").projectDir = File(settingsDir, "./module/module_dev_tools")
include(":-module_spi_default")
project(":-module_spi_default").projectDir = File(settingsDir, "./module/-module_spi_default")

include(":route")
