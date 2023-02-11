@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) // 目前 hilt 暂时还不能支持 ksp https://github.com/google/dagger/issues/2349
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.serializer"
}

dependencies {
    implementation(project(":data-android:model"))
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.google.hilt.library)
    kapt(libs.google.hilt.compiler)
}

