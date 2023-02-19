import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

val keystorePropertiesPath: String = rootProject.file("keystore/keystore.properties").path
val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.google.hilt)
	alias(libs.plugins.kotlin.kapt)
}

android {
	namespace = "dev.shuanghua.weather"

	defaultConfig {
		applicationId = "dev.shuanghua.weather"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
	}

	signingConfigs {
		getByName("debug") {
			storeFile = rootProject.file("debug.keystore")
			keyAlias = "androiddebugkey"
			keyPassword = "android"
			storePassword = "android"
		}

		create("release") { // 创建一个 release 或其它的版本 ，下面的 buildTypes 就能根据创建的名字来获取
			storeFile = rootProject.file(keystoreProperties["storeFile"].toString())
			keyAlias = keystoreProperties["keyAlias"].toString()
			keyPassword = keystoreProperties["keyPassword"].toString()
			storePassword = keystoreProperties["storePassword"].toString()
		}
	}

	buildTypes {
		debug {
			signingConfig = signingConfigs.getByName("debug")
//            isMinifyEnabled = true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}

		release {
			signingConfig = signingConfigs.getByName("release")
			isShrinkResources = true  // 删除没有使用的资源文件 也包括依赖库的资源
			isMinifyEnabled = true  // 删除没有使用的代码 + 缩短名字（混淆）+ 优化
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
}

dependencies {
	implementation(project(":shared"))
	implementation(project(":ui-core:compose"))
	implementation(project(":ui-core:sample"))

	implementation(project(":data-android:model"))
	implementation(project(":data-android:repository"))
	implementation(project(":data-android:network"))
	implementation(project(":data-android:location"))

	// 导肮引用
	implementation(project(":ui-screens:weather"))
	implementation(project(":ui-screens:favorites"))
	implementation(project(":ui-screens:favorites_weather"))
	implementation(project(":ui-screens:more"))
	implementation(project(":ui-screens:province"))
	implementation(project(":ui-screens:city"))
	implementation(project(":ui-screens:district"))
	implementation(project(":ui-screens:station"))
	implementation(project(":ui-screens:settings"))
	implementation(project(":ui-screens:web"))

	implementation(libs.google.hilt.library)
	kapt(libs.google.hilt.compiler)

	// activity
	implementation(libs.androidx.activity.compose)

	// style-material3
	implementation(libs.style.material3)

	implementation(libs.timber)
}