plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.app.base"
    compileSdk = libs.versions.targetSDK.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSDK.get().toInt()

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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.androidx.lifecycle.viewmodel.compose)


    // 启动优化
    api("androidx.startup:startup-runtime:1.1.1")

    // 状态栏 ✅ [https://github.com/gyf-dev/ImmersionBar]
    api("com.geyifeng.immersionbar:immersionbar:3.2.2")
    api("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")

    // 权限动态申请 ✅ [https://github.com/getActivity/XXPermissions]
    api(libs.xxpermissions)

    // 日志打印 ✅ [https://github.com/orhanobut/logger]
    api(libs.logger)

    // 网络状态管理库 ✅ [https://github.com/pwittchen/ReactiveNetwork]
    api(libs.reactivenetwork.rx2)

    // 数据本地化 ✅ [https://github.com/Tencent/MMKV]
    implementation(libs.mmkv)


    // AndroidUtilCode ✅ [https://github.com/Blankj/AndroidUtilCode]
    api(libs.utilcodex) {
        exclude(group = "androidx.appcompat")
    }

    // 多语言切换
    // api 'com.github.getActivity:MultiLanguages:8.0'

    // Toast 消息
    // api(libs.toast.utils)

    // 内存泄露
    debugApi("com.squareup.leakcanary:leakcanary-android:2.14")

    // 后台工作
    // api("androidx.work:work-runtime-ktx:2.9.0")
}