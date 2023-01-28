plugins {
    id("app.android.library")
    id("app.android.library.compose")
}

android {
    namespace = "dev.shuanghua.ui.core.compose"
}

dependencies {
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.layout)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.iconExtended)
    api(libs.androidx.window)
    api(libs.androidx.lifecycle.runtimeCompose)//androidx Lifecycle compose

    implementation(libs.accompanist.systemuicontroller)
    api(libs.accompanist.flowlayout)
    api(libs.accompanist.navigation.animation)
    api(libs.accompanist.permissions)
    api(libs.accompanist.swiperefresh)

    api(libs.coil.compose)
}