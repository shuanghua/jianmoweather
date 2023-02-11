plugins {
    `kotlin-dsl`
}

group = "dev.shuanghua.weather.buildlogic"

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

        register("androidLibrary") { // Library
            id = "android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
    }
}
