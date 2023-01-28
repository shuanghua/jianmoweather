plugins {
    id("app.android.library")
    id("app.android.library.compose")
}

android {
    namespace = "dev.shuanghua.ui.core.sample"
}

dependencies {
    implementation(project(":ui-core:compose"))
}

