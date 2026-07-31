plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "ru.mykvm.navigation"
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt())
        }
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    val xcfName = "base:navigationKit"

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
                //base
                implementation(libs.kotlin.stdlib)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                //ui
                api(projects.base.ui)
                implementation(libs.compose.runtime)
                implementation(libs.compose.uiToolingPreview)
                //navigastion
                implementation(libs.navigation)
                implementation(libs.backhandler)
                //di
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                //screens
                implementation(projects.features.splash.imp)
                implementation(projects.features.space.imp)
                implementation(projects.features.authorization.imp)
                implementation(projects.features.city.imp)
                //language
                implementation(projects.features.language.imp)
                //utils
                implementation(projects.base.utils)
                //annotations
                implementation(libs.annotation)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {}
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
//temp fix for super new gradle and compose for multiplatform
tasks.matching { it.name == "copyAndroidDeviceTestComposeResourcesToAndroidAssets" }.configureEach {
    try {
        val outputDirProp = this::class.members
            .firstOrNull { it.name == "outputDirectory" }
            ?.call(this) as? org.gradle.api.provider.Property<*>

        // Если такое свойство есть и в него еще ничего не записано
        if (outputDirProp != null && !outputDirProp.isPresent) {
            // Создаем провайдер пути для Gradle 9.x
            val buildDirProvider = layout.buildDirectory.dir("intermediates/compose-resources-test-assets")

            // Принудительно передаем значение в Property
            @Suppress("UNCHECKED_CAST")
            (outputDirProp as org.gradle.api.provider.Property<org.gradle.api.file.Directory>).set(buildDirProvider)
        }
    } catch (e: Exception) {
        // Если что-то пошло не так (например, в будущих версиях свойство переименуют),
        // просто отключаем таску, чтобы проект собрался в любом случае
        enabled = false
    }
}