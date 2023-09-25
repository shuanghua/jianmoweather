@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true

//    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.google.ksp)
//    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.serializer"
}

dependencies {
    implementation(project(":data-android:model"))
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
}

