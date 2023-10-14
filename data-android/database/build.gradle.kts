@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
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

    implementation(libs.koin.android)
}