import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.uiToolingPreview)
    //base
    implementation(projects.base.utils)
    //features
    implementation(projects.features.language.imp)
    implementation(projects.features.splash.imp)
    //di
    implementation(libs.koin.android)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "ru.mykvm.pyatak"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.mykvm.pyatak"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}
//если сломаются ресурсы в сплэше
//androidComponents {
//    onVariants { variant ->
//        // Вместо tasks.named() используем прямой путь к build-директории нужного подмодуля
//        // Замените "base:navigation" на точный путь к вашему модулю, если он отличается
//        val navigationModuleBuildDir = project.project(":features:splash:imp").layout.buildDirectory
//
//        variant.sources.assets?.let { assetSources ->
//            // Лениво подключаем директорию скомпилированных ресурсов из модуля навигации
//            assetSources.addStaticSourceDirectory(
//                navigationModuleBuildDir.dir("intermediates/compose-resources-assets/${variant.name}").get().asFile.absolutePath
//            )
//        }
//    }
//}
//
//// Принудительно выстраиваем очередь сборки на глобальном уровне проектов
//// Сначала генерируем ресурсы KMP во всех модулях, затем сливаем их в финальный APK приложения
//project.evaluationDependsOn(":features:splash:imp") // Гарантируем, что модуль навигации уже настроен
//tasks.configureEach {
//    if (name.startsWith("merge") && name.endsWith("Assets")) {
//        // Безопасно подписываемся на таску из другого модуля через rootProject
//        dependsOn(rootProject.tasks.matching { it.name.contains("ComposeResourcesToAndroidAssets") })
//    }
//}