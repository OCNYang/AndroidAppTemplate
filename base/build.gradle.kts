plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.app.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

//    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)

    api(libs.androidx.lifecycle.process)

    // 启动优化
    api("androidx.startup:startup-runtime:1.1.1")

    api(project(":webview-x5"))

    // 权限动态申请 ✅ [https://github.com/getActivity/XXPermissions]
    api(libs.xxpermissions)

    // 日志打印 ✅ [https://github.com/orhanobut/logger]
    api(libs.logger)

    // 网络状态管理库 ✅ [https://github.com/pwittchen/ReactiveNetwork]
    api(libs.reactivenetwork.rx2)

    // 数据本地化 ✅ [https://github.com/Tencent/MMKV]
    implementation(libs.mmkv)

    // 图片加载 ✅ [https://github.com/coil-kt/coil]
    api(libs.coil)
    api(libs.coil.compose){
        exclude(group="androidx.core")
        exclude(group="androidx.activity")
    }

    // AndroidUtilCode ✅ [https://github.com/Blankj/AndroidUtilCode]
    api(libs.utilcodex){
        exclude(group = "androidx.appcompat")
    }

    // 状态栏 ✅ [https://github.com/gyf-dev/ImmersionBar]
    api("com.geyifeng.immersionbar:immersionbar:3.2.2")
    api("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")

    api("com.github.ocnyang:compose-status-box:1.0.1"){
        // exclude(group = "androidx.compose")
    }

    // 多语言切换
    // api 'com.github.getActivity:MultiLanguages:8.0'

    // Toast 消息
    // api(libs.toast.utils)
}