import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

val keystorePropertiesPath: String = rootProject.file("keystore/keystore.properties").path
val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.google.ksp) apply true
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
			storeFile = rootProject.file("keystore/debug.keystore")
			keyAlias = "androiddebugkey"
			keyPassword = "android"
			storePassword = "android"
		}

		/**
		 * 创建 keystore.properties 文件和 YourStoreFileName.jks 签名文件 , 一同放到 keystore 目录下
		 * keystore.properties 文件内容如下:
		 *
		 * 	storePassword=YourStorePassword
		 *  keyPassword=YourKeyPassword
		 *  keyAlias=YourKeyAlias
		 *  storeFile=keystore/YourStoreFileName.jks
		 */
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
		}

		release {
//          请替换你自己的正式签名,并且将 sha-256 散列填到高德定位后台
//			signingConfig = signingConfigs.getByName("release")
			signingConfig = signingConfigs.getByName("debug")
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
	implementation(project(":data-android:domain"))

	// 导肮引用
	implementation(project(":ui-screens:weather"))
	implementation(project(":ui-screens:favorites"))
	implementation(project(":ui-screens:favorites-detail"))
	implementation(project(":ui-screens:more"))
	implementation(project(":ui-screens:province"))
	implementation(project(":ui-screens:city"))
	implementation(project(":ui-screens:district"))
	implementation(project(":ui-screens:station"))
	implementation(project(":ui-screens:settings"))
	implementation(project(":ui-screens:web"))

	implementation(libs.koin.android)

	// activity
	implementation(libs.androidx.activity.compose)

	// style-material3
	implementation(libs.style.material3)

	implementation(libs.timber)
}