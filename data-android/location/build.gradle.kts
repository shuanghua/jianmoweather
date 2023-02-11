@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.location"
}

dependencies {
    implementation(libs.ali.location)

    implementation(libs.google.hilt.library)
    kapt(libs.google.hilt.compiler)
}