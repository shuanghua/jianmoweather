@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
}

android {
    namespace = "dev.shuanghua.weather.data.android.domain"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:repository"))
    implementation(libs.koin.android)
}