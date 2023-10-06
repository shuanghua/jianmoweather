@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.location"
}

dependencies {
    implementation(libs.ali.location)

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
}