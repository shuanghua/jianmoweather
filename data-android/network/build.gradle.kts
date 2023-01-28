@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    alias(libs.plugins.google.ksp)
    id("app.android.hilt")
}

android {
    namespace = "dev.shuanghua.weather.data.android.network"
}

dependencies {

    implementation(project(":shared"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:serializer"))


    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
}