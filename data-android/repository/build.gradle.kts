@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    id("app.android.hilt")
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "dev.shuanghua.weather.data.android.repository"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:network"))
    implementation(project(":data-android:database"))
    implementation(project(":data-android:datastore"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:location"))
}