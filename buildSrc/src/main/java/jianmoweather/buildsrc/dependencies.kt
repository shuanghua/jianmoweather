package jianmoweather.buildsrc

object Libs {
    private const val kotlinGradleVersion = "1.6.10"
//    private const val kotlinGradleVersion = "1.6.20-RC2"

    private const val androidGradleVersion = "7.1.2"
    private const val hiltGradleVersion = "2.41"

    const val androidPlugin = "com.android.tools.build:gradle:$androidGradleVersion"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinGradleVersion"
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltGradleVersion"

    const val material3 = "com.google.android.material:material:1.6.0-alpha02"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    object Kotlin {
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinGradleVersion"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
        const val navigation = "androidx.navigation:navigation-compose:2.5.0-alpha02"

        object Core {
            private const val version = "1.4.0-alpha01"
            const val core = "androidx.core:core:$version"
            const val coreKtx = "androidx.core:core-ktx:$version"
        }

        object Activity {
            private const val version = "1.3.0-alpha06"
            const val activityKtx = "androidx.activity:activity-ktx:$version"
            const val activityCompose = "androidx.activity:activity-compose:$version"
        }

        object Fragment {
            private const val version = "1.2.0-beta02"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.5.0-alpha04"
            // val model: MyViewModel by viewModels() // ktx： 通过 by 关键字来生成
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"

            //  在 Activity / Fragment 中使用原始方式创建 ViewModel:
            //  viewModel = ViewModelProvider(this)[ExampleViewModel::class.java] //defaultFactory()

            // 在 Compose 函数中创建 Activity/Fragment ViewModel:
            // viewModel: ExampleViewModel = viewModel()
            // viewModel: ExampleViewModel = viewModel(object():ViewModelProvider.Factory)
            const val viewModelInCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"

        }

        object Room {
            private const val version = "2.4.2"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"//kapt
            const val ktx = "androidx.room:room-ktx:$version"//使用flow
        }

        object Compose {
            //https://maven.google.com/web/index.html?q=Compose#androidx.compose.foundation:foundation
            const val version = "1.2.0-alpha07"
            const val core = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            object Material3{
                //https://maven.google.com/web/index.html?q=material#androidx.compose.material3:material3
                const val version = "1.0.0-alpha09"
                const val material3 = "androidx.compose.material3:material3:${version}"
            }
        }

        object Test {
            //本地单元测试
            //const val junit = "junit:junit:4.12"
            const val junitExt = "androidx.test.ext:junit:1.1.1"
            const val robolectric = "androidx.test:core:1.3.0-rc01"
            const val mockito = "org.mockito:mockito-core:1.10.19"

            //ui 测试
            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"

        }
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:$hiltGradleVersion"
        const val compiler = "com.google.dagger:hilt-compiler:$hiltGradleVersion"

        object AndroidX{
            private const val version = "1.0.0"
            // 如果使用 Fragment 或者 Activity 的 ViewModel
//            const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
//            const val compiler = "androidx.hilt:hilt-compiler:$version"

            // 如果使用 Navigation-Compose 的 ViewModel
            // Compose 页面的生命周期和导航有关系，所以推荐使用导航相关的 Viewmodel.
            // 不推荐使用 Fragment 或者 Activity 的 ViewModel
            const val navViewModelInCompose = "androidx.hilt:hilt-navigation-compose:$version"
        }
    }

    object Accompanist {
        // https://google.github.io/accompanist/
        private const val version = "0.24.6-alpha"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val flowLayouts = "com.google.accompanist:accompanist-flowlayout:$version"
        const val animation = "com.google.accompanist:accompanist-navigation-animation:$version"
        const val permissions =  "com.google.accompanist:accompanist-permissions:$version"
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
    }

    object AliBaBa {
        const val gaoDeLocation = "com.amap.api:location:latest.integration"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converterMoShi = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object MoShi {
        private const val version = "1.13.0"
        const val moshi = "com.squareup.moshi:moshi:$version"
        const val kotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"//kapt 解析
        const val adapter = "com.squareup.moshi:moshi-adapters:$version"
    }

    object OkHttp {
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.2"
    }

    object LeakCanary {
        private const val version = "2.8.1"
        const val android = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    object Coil {
        private const val version = "0.13.0"
        const val coil = "io.coil-kt:coil:$version"

        const val coilCompose = "io.coil-kt:coil-compose:2.0.0-rc01"
    }

    object Permission{
        private const val version = "1.0.0"
        const val easyPermissions = "com.vmadalin:easypermissions-ktx:$version"
    }
}