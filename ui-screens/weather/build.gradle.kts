plugins {
    alias(libs.plugins.jianmoweather.android.library)
}


android { // 版本号提取到了 project -> build.gradle.kts
    namespace = "dev.shuanghua.ui.screen.weather"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}


dependencies {
    implementation(libs.koin.android.compose)

    implementation(project(":shared"))
    implementation(project(":ui-core:compose"))
    implementation(project(":ui-core:components"))

    implementation(project(":data-android:model"))
    implementation(project(":data-android:domain"))
    implementation(project(":data-android:repository"))
    implementation(project(":data-android:network"))
    implementation(project(":data-android:location"))
}