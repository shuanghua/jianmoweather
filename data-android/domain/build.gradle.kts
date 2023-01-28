plugins {
    id("app.android.library")
    id("app.android.hilt")
}

android {
    namespace = "dev.shuanghua.weather.data.android.domain"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:repository"))
}