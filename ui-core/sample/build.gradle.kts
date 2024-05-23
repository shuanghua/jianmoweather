plugins {
    alias(libs.plugins.jianmoweather.compose.android.library)
}

android {
    namespace = "dev.shuanghua.ui.core.sample"
}

dependencies {
    implementation(project(":ui-core:compose"))
}

