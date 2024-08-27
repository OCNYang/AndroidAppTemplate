// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.compose) apply false
//    alias(libs.plugins.kotlin.parcelize) apply false
    id("org.jetbrains.kotlin.plugin.serialization").version("1.6.21")
}