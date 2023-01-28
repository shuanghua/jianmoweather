plugins {
    `kotlin-dsl`
}

group = "dev.shuanghua.weather.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_11.toString().toInt())
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}


gradlePlugin {
    plugins {

        register("androidLibrary") { // Library
            id = "app.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }

        register("hilt") {  // hilt + kapt
            id = "app.android.hilt"
            implementationClass = "HiltPlugin"
        }

        register("composeApplication") {
            id = "app.android.application.compose"
            implementationClass = "ComposeApplicationPlugin"
        }

        register("composeLibrary") {
            id = "app.android.library.compose"
            implementationClass = "ComposeLibraryPlugin"
        }

        register("uiScreen") { //androidLibrary + hilt + kapt + compose -> build.gradle + navigation + data + shared
            id = "app.android.library.screen"
            implementationClass = "ScreenPlugin"
        }
    }
}
