rootProject.name = "Pyatak"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":androidApp")
include(":shared")
include(":base:ui")
include(":base:navigation")
include(":base:network")
include(":base:notifications")
include(":base:storage")
include(":base:utils")
include(":base:viewmodel")
include(":features:asmode:api")
include(":features:asmode:imp")
include(":features:authorization:api")
include(":features:authorization:imp")
include(":features:splash:api")
include(":features:splash:imp")
include(":features:space:api")
include(":features:space:imp")
include(":features:city:api")
include(":features:city:imp")
