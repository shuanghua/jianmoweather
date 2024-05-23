plugins {
    alias(libs.plugins.jianmoweather.compose.android.library)
}


android {
    namespace = "dev.shuanghua.ui.screen.more"
}


dependencies {
//    implementation(libs.google.hilt.library)
//    kapt(libs.google.hilt.compiler)

    implementation(libs.koin.android.compose)

    implementation(project(":shared"))
    implementation(project(":ui-core:compose"))
    implementation(project(":ui-core:components"))

    implementation(project(":data-android:domain"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:repository"))
    implementation(project(":data-android:network"))
    implementation(project(":data-android:location"))
}