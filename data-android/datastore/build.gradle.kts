plugins {
    alias(libs.plugins.jianmoweather.android.library)
    alias(libs.plugins.google.ksp) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.datastore"
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:model"))

    implementation(libs.protobuf.dataStore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.koin.android)
}

//protobuf {
//    protoc {
//        artifact = libs.protobuf.protoc.get().toString()
//    }
//    generateProtoTasks {
//        all().forEach { task ->
//            task.builtins {
//                val java by registering {
//                    option("lite")
//                }
//                val kotlin by registering {
//                    option("lite")
//                }
//            }
//        }
//    }
//}