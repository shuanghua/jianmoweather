import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}


group = "dev.shuanghua.apps.buildlogic"


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.toString().toInt())
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}


gradlePlugin {
    plugins {
        register("BaseAndroidLibrary") { // Library
            id = "jianmoweather.android.library"
            implementationClass = "AndroidLibraryPlugin"
            group = "dev.shuanghua.apps.buildlogic"
        }

        register("ComposeAndroidLibrary") { // Library
            id = "jianmoweather.compose.android.library"
            implementationClass = "AndroidComposeLibraryPlugin"
        }
    }
}
