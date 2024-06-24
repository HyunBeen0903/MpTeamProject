// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin) apply false

}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Add this line
        classpath ("com.google.gms:google-services:4.3.10")
        classpath ("com.android.tools.build:gradle:8.0.0")  // 최신 버전 확인 후 설정
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")  // 최신 버전 확인 후 설정
    }
}
