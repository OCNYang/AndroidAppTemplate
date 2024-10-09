plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose)
    alias(libs.plugins.googleKsp)
}

android {
    namespace = "com.app.module_main"
    compileSdk = libs.versions.targetSDK.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSDK.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    ksp {
        arg("compose-destinations.mode", "navgraphs")
        arg("compose-destinations.moduleName", "main")
        arg("compose-destinations.useComposableVisibility", "true")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.paging.compose)

    implementation(project(":base"))
    implementation(project(":route"))
    implementation(project(":library_ui_uniform"))
    implementation(project(":api_main"))
    implementation(project(":library_webview"))

    ksp(libs.compose.destinations.ksp)
}