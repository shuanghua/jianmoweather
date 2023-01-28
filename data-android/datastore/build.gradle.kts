@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    alias(libs.plugins.google.ksp) // 目前 hilt 暂时还不能支持ksp https://github.com/google/dagger/issues/2349
    id("app.android.hilt")
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
    implementation(project(":data-android:serializer"))

    implementation(libs.protobuf.dataStore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
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