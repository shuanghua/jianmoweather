plugins{
    id("kotlin")
    alias(libs.plugins.kotlin.serialization)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    implementation(libs.koin.android)
}