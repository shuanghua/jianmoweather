@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("android.library")
    alias(libs.plugins.google.ksp) apply true
}

android {
    namespace = "dev.shuanghua.weather.data.android.repository"
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data-android:network"))
    implementation(project(":data-android:database"))
    implementation(project(":data-android:datastore"))
    implementation(project(":data-android:model"))
    implementation(project(":data-android:location"))
    testImplementation(project(":data-android:testing"))

    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)
}