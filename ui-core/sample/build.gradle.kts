plugins {
    alias(libs.plugins.jianmoweather.android.library)
}

android {
    namespace = "dev.shuanghua.ui.core.sample"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":ui-core:compose"))
}

