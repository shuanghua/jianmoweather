plugins {
    alias(libs.plugins.jianmoweather.android.library)
}

android {
    namespace = "dev.shuanghua.weather.data.android.location"
}

dependencies {
    api(libs.ali.location)
    implementation(libs.koin.android)
}