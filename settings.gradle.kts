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
        maven { url = uri("https://maven.aliyun.com/repository/public") } // 添加阿里云仓库
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/public") } // 添加阿里云仓库
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "AIProject"
include(":dependencies:service")
include(":dependencies:common")
include(":dependencies:statemanager")
include(":dependencies:zip")
include(":dependencies:quickadapter")
include(":dependencies:permission")
//include(":dependencies:selector")
//include(":dependencies:compress")
//include(":dependencies:ucrop")
include(":dependencies:appcore")
include(":dependencies:ui")
include(":dependencies:proto")
include(":dependencies:jsbridge")

include(":app")
include(":feature:container")
include(":feature:mine")
include(":feature:login")