@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true

//    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.testing"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.test.junit4)
    api(libs.test.androidCore)
    api(libs.test.coroutines)

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
}