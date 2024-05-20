plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.gradlePluginPublish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.3.0")
    compileOnly("com.android.tools.build:gradle-api:8.3.0")
    compileOnly("org.ow2.asm:asm-commons:9.6")
    // compileOnly("org.ow2.asm:asm-util:9.6")
    // compileOnly("org.ow2.asm:asm:9.6")
}