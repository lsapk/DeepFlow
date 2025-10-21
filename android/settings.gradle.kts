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
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

rootProject.name = "android"
include(":app")
include(":capacitor-android")
project(":capacitor-android").projectDir = file("../node_modules/@capacitor/android/capacitor")
include(":capacitor-cordova-android-plugins")
project(":capacitor-cordova-android-plugins").projectDir = file("capacitor-cordova-android-plugins")
