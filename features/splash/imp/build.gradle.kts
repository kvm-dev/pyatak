plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "ru.mykvm.splash.imp"
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt())
        }
        minSdk = libs.versions.android.minSdk.get().toInt()

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

        androidResources.enable = true

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    val xcfName = "features:splash:impKit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                //api
                api(projects.features.splash.api)
                //viewmodel
                api(projects.base.viewmodel)
                //ui
                implementation(projects.base.ui)
                //base libs
                implementation(libs.kotlin.stdlib)
                //encrypted storage
                implementation(projects.base.storage)
                //utils
                implementation(projects.base.utils)
                //di
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                //features
                implementation(projects.features.language.api)
                implementation(projects.features.authorization.api)
//                implementation(projects.features.networkconnection.api)
//                implementation(projects.features.profile.api)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                //di
                implementation(libs.koin.android)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.runner)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {}
        }
    }
}

    compose.resources {
        publicResClass = true
        packageOfResClass = "ru.mykvm.splash.imp.ComposeResources"
        generateResClass = always
    }