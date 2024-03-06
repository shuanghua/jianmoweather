plugins {
    alias(libs.plugins.jianmoweather.android.library)
}

android {
    namespace = "dev.shuanghua.weather.data.android.repository"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:network"))
    implementation(project(":data-android:location"))
    implementation(project(":data-android:database"))
    implementation(project(":data-android:datastore"))
    implementation(project(":data-android:model"))
    testImplementation(project(":data-android:testing"))
    implementation(libs.koin.android)
}