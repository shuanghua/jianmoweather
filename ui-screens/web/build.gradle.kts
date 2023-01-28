plugins {
    id("app.android.library.screen")

}

android {
    namespace = "dev.shuanghua.ui.web"
}

dependencies {
    implementation(libs.accompanist.webview)
}