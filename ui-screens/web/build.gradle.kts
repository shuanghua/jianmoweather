plugins {
    alias(libs.plugins.jianmoweather.compose.android.library)
}

android {
    namespace = "dev.shuanghua.ui.screen.web"
}

dependencies {
    implementation(libs.accompanist.webview)
    implementation(project(":shared"))
    implementation(project(":ui-core:compose"))
}