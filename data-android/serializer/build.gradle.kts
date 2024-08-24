plugins{
    id("kotlin")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}