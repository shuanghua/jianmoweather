import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import dagger.hilt.android.plugin.HiltExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("DSL_SCOPE_VIOLATION") //https://github.com/gradle/gradle/issues/22797
plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.kotlin.kapt) apply false
	alias(libs.plugins.google.hilt) apply false
	alias(libs.plugins.protobuf.plugin) apply false
}

tasks.register("clean") {
	delete(rootProject.buildDir)
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}

	val javaVersion = JavaVersion.VERSION_17

//        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//        kotlinOptions {
//            jvmTarget = javaVersion.toString()
//        }
//    }

	plugins.withType<JavaBasePlugin>().configureEach {
		extensions.configure<JavaPluginExtension> {
			toolchain {
				languageVersion.set(JavaLanguageVersion.of(17))
			}
		}
	}

	// Configure Hilt + Dagger
	plugins.withId(rootProject.libs.plugins.google.hilt.get().pluginId) {
		extensions.getByType<HiltExtension>().enableAggregatingTask = true
	}
	plugins.withId(rootProject.libs.plugins.kotlin.kapt.get().pluginId) {
		extensions.getByType<KaptExtension>().correctErrorTypes = true
	}

	// Android 配置
	plugins.withType<BasePlugin>().configureEach {
		extensions.configure<BaseExtension> {
			compileSdkVersion(libs.versions.compileSdk.get().toInt())

			defaultConfig {
				targetSdk = libs.versions.targetSdk.get().toInt()
				minSdk = libs.versions.minSdk.get().toInt()
				versionCode = 1
				versionName = "1.0"
			}
			compileOptions {
				sourceCompatibility = javaVersion
				targetCompatibility = javaVersion
			}
		}
	}


	// Compose 生成重组统计文件
	tasks.withType<KotlinCompilationTask<*>>().configureEach {
		compilerOptions {
//            allWarningsAsErrors = true         // 警告转成错误输出,会中断编译, 0警告强迫症使用
//            freeCompilerArgs.addAll(
//                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi" // ExperimentalCoroutinesApi 当使用时取消注释, 目前 协程 居于稳定,基本用不到了
//            )

			// 命令输入 ./gradlew assembleRelease -P jianmoweather.enableComposeCompilerReports=true
			if (project.hasProperty("jianmoweather.enableComposeCompilerReports")) {
				freeCompilerArgs.addAll(
					"-P",
					"plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
							project.buildDir.absolutePath + "/compose_metrics"
				)

				freeCompilerArgs.addAll(
					"-P",
					"plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
							project.buildDir.absolutePath + "/compose_metrics"
				)
			}
		}
	}
}