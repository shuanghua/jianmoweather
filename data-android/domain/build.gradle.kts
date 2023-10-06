@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.domain"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:repository"))

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
}