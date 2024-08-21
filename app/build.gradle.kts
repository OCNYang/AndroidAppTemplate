plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose)
    alias(libs.plugins.googleKsp)
}

android {
    namespace = "com.app.template"
    compileSdk = libs.versions.targetSDK.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get() // TODO: change it
        minSdk = libs.versions.minSDK.get().toInt()
        targetSdk = libs.versions.targetSDK.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 全局强制统一版本
//configurations.all {
//    resolutionStrategy {
//        force("androidx.compose.ui:ui-android:1.7.0-beta07")
//    }
//}

// 全局排除
//configurations {
//    implementation {
//        exclude("androidx.compose.ui", "ui")
//    }
//}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)

    implementation(project(":base"))

    implementation(project(":api_base"))
    implementation(project(":api_main"))
    implementation(project(":api_main_imp"))
    implementation(project(":library_webview"))
    implementation(project(":library_image_load"))
    implementation(project(":library_ui_uniform"))


    implementation(project(":library_glance"))
    runtimeOnly(project(":library_network"))

//    implementation(libs.androidx.constraintlayout.compose)
//    implementation(libs.androidx.compose.runtime)
//    implementation(libs.androidx.compose.foundation)
//    implementation(libs.androidx.compose.foundation.layout)
//    implementation(libs.androidx.compose.runtime.livedata)
//    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.core.splashscreen)


    // 路由导航封装库
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)
    // for bottom sheet destination support, also add
    implementation(libs.compose.destinations.bottom.sheet)
}