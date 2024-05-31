plugins {
    alias(libs.plugins.jianmoweather.android.library)
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.network"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))

    testImplementation(project(":data-android:testing"))

    implementation(libs.kotlin.serialization.json)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlin.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.koin.android)
    
}