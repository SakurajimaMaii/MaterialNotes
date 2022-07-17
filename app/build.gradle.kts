plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Versions.compile_sdk_version
    buildToolsVersion = Versions.build_tools_version

    defaultConfig {
        applicationId = "com.gcode.materialnotes"
        minSdk = Versions.min_sdk_version
        targetSdk = Versions.target_sdk_version
        versionCode = Versions.version_code
        versionName = Versions.version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    // 自定义工具类
    implementation(files("libs/VastTools_0.0.9_Cancey.jar"))
    // 自定义适配器
    implementation(files("libs/VastAdapter_0.0.6_Cancey.jar"))
    // 换肤框架
    implementation(files("libs/VastSkin_0.0.1_Cancey.jar"))
    implementation(Deps.dependencies_activity_ktx)
    implementation(Deps.dependencies_appcompat)
    implementation(Deps.dependencies_constraintlayout)
    implementation(Deps.dependencies_core_ktx)
    implementation(Deps.dependencies_core_splashscreen)
    implementation(Deps.dependencies_kotlin_coroutines_android)
    implementation(Deps.dependencies_lifecycle_livedata_ktx)
    implementation(Deps.dependencies_lifecycle_runtime_ktx)
    implementation(Deps.dependencies_lifecycle_viewmodel_ktx)
    implementation(Deps.dependencies_material)
    implementation(Deps.dependencies_preference_ktx)
    implementation(Deps.dependencies_room_ktx)
    implementation(Deps.dependencies_room_runtime)
    annotationProcessor(Deps.dependencies_room_compiler)
    androidTestImplementation(Deps.dependencies_ext_junit)
    androidTestImplementation(Deps.dependencies_espresso_core)
    testImplementation(Deps.dependencies_junit)
    kapt(Deps.dependencies_room_compiler)
}
