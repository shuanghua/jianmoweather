import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties


plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.google.ksp)
	alias(libs.plugins.compose.compiler)
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

	signingConfigs {
		getByName("debug") {
			if(rootProject.file("keystore/debug_keystore.properties").exists()){
				val keystorePropertiesPath: String = rootProject.file("keystore/debug_keystore.properties").path
				val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)
				storeFile = rootProject.file(keystoreProperties["storeFile"].toString())
				keyAlias = keystoreProperties["keyAlias"].toString()
				keyPassword = keystoreProperties["keyPassword"].toString()
				storePassword = keystoreProperties["storePassword"].toString()
			}
		}

		create("release") { // 创建一个 release 或其它的版本 ，下面的 buildTypes 就能根据创建的名字来获取
			if(rootProject.file("keystore/release_keystore.properties").exists()){
				val keystorePropertiesPath: String = rootProject.file("keystore/release_keystore.properties").path
				val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)
				storeFile = rootProject.file(keystoreProperties["storeFile"].toString())
				keyAlias = keystoreProperties["keyAlias"].toString()
				keyPassword = keystoreProperties["keyPassword"].toString()
				storePassword = keystoreProperties["storePassword"].toString()
			}

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