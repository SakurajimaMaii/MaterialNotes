import cn.govast.plugin.version.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("cn.govast.plugin.version")
}

android {
    compileSdk = Version.compile_sdk_version
    buildToolsVersion = Version.build_tools_version

    defaultConfig {
        applicationId = "com.gcode.materialnotes"
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf("room.schemaLocation" to "$projectDir/schemas".toString()))
            }
        }
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

    namespace = "com.gcode.materialnotes"

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.core_splashscreen)
    implementation(Jetbrains.kotlinx_coroutines_android)
    implementation(AndroidX.lifecycle_livedata_ktx)
    implementation(AndroidX.lifecycle_runtime_ktx)
    implementation(AndroidX.lifecycle_viewmodel_ktx)
    implementation(Google.material)
    implementation(AndroidX.preference_ktx)
    implementation(AndroidX.room_ktx)
    implementation(AndroidX.room_runtime)
    annotationProcessor(AndroidX.room_compiler)
    androidTestImplementation(Libraries.junit)
    androidTestImplementation(AndroidX.espresso_core)
    kapt(AndroidX.room_compiler)
}
