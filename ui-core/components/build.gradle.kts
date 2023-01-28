plugins {
    id("app.android.library")
    id("app.android.library.compose")
}

android {
    namespace = "dev.shuanghua.ui.core.components"
}

dependencies {
    implementation(project(':ui-core:compose'))
    implementation(project(":shared"))
    implementation(project(":data-android:model"))
}