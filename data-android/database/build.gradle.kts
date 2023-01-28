@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    id("app.android.hilt")
    alias(libs.plugins.google.ksp)
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
}