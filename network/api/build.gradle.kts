plugins {
    id("java-library")
    id("com.google.devtools.ksp").version("1.9.21-1.0.15")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // json 解析库 ✅
    api("com.squareup.moshi:moshi:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0") // 代码生成
    // 反射使用 todo 无用；todo 如果使用必须添加 @JsonClass(generateAdapter = false) 防止被混淆
    api("com.squareup.moshi:moshi-kotlin:1.14.0")
}