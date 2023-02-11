@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
//    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.shared"
}

dependencies {
//    implementation(libs.google.hilt.library)
//    kapt(libs.google.hilt.compiler)
}

