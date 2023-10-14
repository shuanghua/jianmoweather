@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.network"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))

    testImplementation(project(":data-android:testing"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.koin.android)
    
}