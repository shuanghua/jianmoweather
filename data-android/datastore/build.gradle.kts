import org.jetbrains.kotlin.wasm.ir.opcodesToOp

plugins {
    alias(libs.plugins.jianmoweather.android.library)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.protobuf.plugin) apply true

}

android {
    namespace = "dev.shuanghua.weather.data.android.datastore"
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))

    implementation(libs.protobuf.dataStore)
    implementation(libs.protobuf.dataStore.core)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.koin.android)
}