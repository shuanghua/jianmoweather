plugins {
    id("app.android.library")
    id("app.android.hilt")
}

android {
    namespace = "dev.shuanghua.weather.data.android.location"
}

dependencies {
    implementation(libs.ali.location)
}