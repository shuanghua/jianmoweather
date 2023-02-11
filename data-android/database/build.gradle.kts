@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "dev.shuanghua.weather.data.android.database"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.google.hilt.library)
    kapt(libs.google.hilt.compiler)
}