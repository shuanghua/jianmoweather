object Libs {

    private const val version_plugin_kotlin = "1.6.20"
	private const val version_plugin_android = "7.3.0-alpha09"
	private const val version_plugin_hilt = "2.41"


    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version_plugin_kotlin"
    const val android_plugin = "com.android.tools.build:gradle:$version_plugin_android"
    const val hilt_plugin = "com.google.dagger:hilt-android-gradle-plugin:$version_plugin_hilt"


    private const val version_material = "1.6.0-alpha02"
    const val material = "com.google.android.material:material:$version_material"


    // Coroutines
    private const val version_kotlin_coroutines = "1.6.1"
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version_kotlin_coroutines"
    const val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version_kotlin_coroutines"


    private const val version_core = "1.6.0-alpha01"
    const val appcompat = "androidx.appcompat:appcompat:$version_core"


    //https://developer.android.com/jetpack/androidx/releases/lifecycle
    private const val version_lifecycle = "2.5.0-beta01"
    const val lifecycle_scope = "androidx.lifecycle:lifecycle-runtime-ktx:$version_lifecycle" // only lifecycle (repeatOnLifecycle, flowWithLifecycle)
    const val viewmodel_scope = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version_lifecycle" // viewModelScope.launch{}
    // const val compose_viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$version_lifecycle"
    // const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0:$version_lifecycle" // livedata


    // Compose
    // https://maven.google.com/web/index.html?q=Compose#androidx.compose.foundation:foundation
    // https://maven.google.com/web/index.html?q=material#androidx.compose.material3:material3

    const val compose_ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val compose_layout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"

    private const val version_compose_material3 = "1.0.0-alpha10"
    const val compose_material3 = "androidx.compose.material3:material3:$version_compose_material3"

    private const val version_compose_activity = "1.6.0-alpha01"
    const val compose_activity = "androidx.activity:activity-compose:$version_compose_activity"
    const val compose_activity_ktx = "androidx.activity:activity-ktx:$version_compose_activity"

    private const val version_compose_navigation = "2.5.0-beta01"
    const val compose_navigation = "androidx.navigation:navigation-compose:$version_compose_navigation"


    private const val version_room = "2.5.0-alpha01"
    const val room_runtime = "androidx.room:room-runtime:$version_room"
    const val room_compiler = "androidx.room:room-compiler:$version_room"//kapt 或 ksp
    const val room_ktx = "androidx.room:room-ktx:$version_room"//使用flow


	private const val version_hilt = version_plugin_hilt
    const val hilt_android = "com.google.dagger:hilt-android:$version_hilt"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:$version_hilt" //暂不支持 ksp
    const val hilt_compose_navigation_viewmodel = "androidx.hilt:hilt-navigation-compose:1.0.0"


    private const val version_retrofit = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$version_retrofit"
    const val retrofit_converter_moshi = "com.squareup.retrofit2:converter-moshi:$version_retrofit"


    private const val moshiVersion = "1.13.0"
    const val moshi = "com.squareup.moshi:moshi:$moshiVersion"
    const val moshi_codegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"//kapt 解析
    const val moshi_adapter = "com.squareup.moshi:moshi-adapters:$moshiVersion" // 反射


    private const val version_coil = "0.13.0"
    const val coil = "io.coil-kt:coil:$version_coil"


	private const val version_coil_compose = "2.0.0-rc03"
    const val coil_compose = "io.coil-kt:coil-compose:$version_coil_compose"


	private const val version_timber = "4.7.1"
    const val timber = "com.jakewharton.timber:timber:$version_timber"


	private const val version_okhttp_logging = "4.9.2"
    const val okhttp_logging = "com.squareup.okhttp3:logging-interceptor:$version_okhttp_logging"


    private const val version_leakcanary = "2.8.1"
    const val leakcanary_android = "com.squareup.leakcanary:leakcanary-android:$version_leakcanary"


    const val gaode_location = "com.amap.api:location:latest.integration"


    // Accompanist 是一组库，旨在补充 Jetpack Compose 的功能 , 以后可能会把部分功能集成到 compose 中
    // https://google.github.io/accompanist/
    // compose: 运行时权限 ,下拉刷新 , 流式布局 , 动画等
    private const val version_accompanist = "0.24.6-alpha"
    const val compose_flowlayouts = "com.google.accompanist:accompanist-flowlayout:$version_accompanist"
    const val compose_navigation_animation = "com.google.accompanist:accompanist-navigation-animation:$version_accompanist"
    const val compose_swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version_accompanist"
    const val compose_permissions = "com.google.accompanist:accompanist-permissions:$version_accompanist"
    const val compose_systembar_controller = "com.google.accompanist:accompanist-systemuicontroller:$version_accompanist"



    //本地单元测试
    //const val junit = "junit:junit:4.12"
    const val junitExt = "androidx.test.ext:junit:1.1.1"
    const val robolectric = "androidx.test:core:1.3.0-rc01"
    const val mockito = "org.mockito:mockito-core:1.10.19"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
}