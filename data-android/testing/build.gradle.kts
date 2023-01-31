@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    alias(libs.plugins.google.ksp)
    id("app.android.hilt")
}

android {
    namespace = "dev.shuanghua.weather.data.android.testing"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(libs.test.junit4)
    api(libs.test.androidCore)
    api(libs.test.coroutines)
}