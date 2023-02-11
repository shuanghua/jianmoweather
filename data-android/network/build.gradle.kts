@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.network"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:serializer"))

    testImplementation(project(":data-android:testing"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.google.hilt.library)
    kapt(libs.google.hilt.compiler)
}