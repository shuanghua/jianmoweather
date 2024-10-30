import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin

plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.google.ksp) apply false
	alias(libs.plugins.compose.compiler) apply false
}

tasks.register("clean") {
	delete(rootProject.layout.buildDirectory)
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}


	plugins.withType<JavaBasePlugin>().configureEach {
		extensions.configure<JavaPluginExtension> {
			toolchain {
				languageVersion.set(JavaLanguageVersion.of(17)) // 需要和 gradle-jdk 版本一致
			}
		}
	}


	// Android 配置
	plugins.withType<BasePlugin>().configureEach {
		extensions.configure<BaseExtension> {
			compileSdkVersion(libs.versions.compileSdk.get().toInt())

			defaultConfig {
				targetSdk = libs.versions.targetSdk.get().toInt()
				minSdk = libs.versions.minSdk.get().toInt()
				versionCode = libs.versions.versionCode.get().toInt()
				versionName =  libs.versions.versionName.get()
			}

			compileOptions {
				sourceCompatibility = JavaVersion.VERSION_17
				targetCompatibility = JavaVersion.VERSION_17
			}
		}
	}
}