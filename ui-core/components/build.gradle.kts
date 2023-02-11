plugins {
    id("android.library")  // android-library kotlin coroutines timber
}

android {
    namespace = "dev.shuanghua.ui.core.components"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":ui-core:compose"))
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
}