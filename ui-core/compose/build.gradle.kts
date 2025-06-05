plugins {
    alias(libs.plugins.jianmoweather.compose.android.library)
}

android {
    namespace = "dev.shuanghua.ui.core.compose"
}

dependencies {
    api(platform(libs.androidx.compose.bom)) // 基础包
    api(libs.androidx.compose.ui.tooling) // 预览
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material.iconExtended)
    api(libs.androidx.compose.navigation)


    api(libs.androidx.window)
    api(libs.androidx.lifecycle.runtimeCompose) // uiState.collectAsStateWithLifecycle()

    implementation(libs.accompanist.systemuicontroller)
    api(libs.accompanist.flowlayout)
//    api(libs.accompanist.navigation.animation)
    api(libs.accompanist.permissions)
    api(libs.accompanist.swiperefresh)

    api(libs.coil.compose)
}