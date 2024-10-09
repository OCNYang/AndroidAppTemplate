plugins {
    id("java-library")
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // json 解析库 ✅
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen) // 代码生成
    // 反射使用 todo 无用；todo 如果使用必须添加 @JsonClass(generateAdapter = false) 防止被混淆
    api(libs.moshi.kotlin)
}