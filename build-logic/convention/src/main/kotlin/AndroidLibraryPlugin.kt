import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * AndroidLibrary + Kotlin + Coroutines + Timber
 * 虽 Android 项目现在已经自带了 Kotlin 库 和 插件, 不需要在 dependencies 中添加依赖
 * 但本项目使用 compose ，为匹配版本，所以手动添加依赖指定的 kotlin 版本
 */
class AndroidLibraryPlugin : Plugin<Project> {
	override fun apply(target: Project) {

		with(target) {
			with(pluginManager) {
				apply("com.android.library")
				apply("org.jetbrains.kotlin.android")
			}

			extensions.configure<LibraryExtension> {
				defaultConfig.targetSdk = 34
//				configureKotlinAndroid(this)
			}

			val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
			dependencies {
				add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
				add("implementation", libs.findLibrary("timber").get())
			}
		}
	}
}
