plugins {
    id("android.library")
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
    implementation(libs.google.hilt.compose.navigation)
    implementation(project(":shared"))
    implementation(project(":ui-core:compose"))
}