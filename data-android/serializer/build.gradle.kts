@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("app.android.library")
    id("app.android.hilt")
    alias(libs.plugins.google.ksp) // 目前 hilt 暂时还不能支持 ksp https://github.com/google/dagger/issues/2349

}

android {
    namespace = "dev.shuanghua.weather.data.android.serializer"
}

dependencies {
    implementation(project(":data-android:model"))
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
}

