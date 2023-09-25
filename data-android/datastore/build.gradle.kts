@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
//    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.google.hilt)
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

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
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