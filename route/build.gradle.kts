plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    // alias(libs.plugins.googleKsp)
}

android {
    namespace = "com.app.route"
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
}

dependencies {

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // 路由导航封装库
    api(libs.compose.destinations.core)
    // ksp(libs.compose.destinations.ksp)
    // for bottom sheet destination support, also add
    api(libs.compose.destinations.bottom.sheet)
//    api(libs.androidx.navigation.compose)
}