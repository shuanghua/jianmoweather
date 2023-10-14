@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
}

android {
    namespace = "dev.shuanghua.weather.data.android.location"
}

dependencies {
    api(libs.ali.location)
    implementation(libs.koin.android)
}