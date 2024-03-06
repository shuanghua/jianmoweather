plugins {
    alias(libs.plugins.jianmoweather.android.library)
}

android {
    namespace = "dev.shuanghua.ui.screen.web"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(libs.accompanist.webview)
    implementation(project(":shared"))
    implementation(project(":ui-core:compose"))
}