pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Squirrel"
include(":app")
include(":screen-home")
include(":bottom-tabs")
include(":squirrel-navigation")
include(":squirrel-data")
include(":squirrel-utils")
include(":squirrel-drive")
include(":squirrel-setting")
