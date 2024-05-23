import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType


class AndroidComposeLibraryPlugin : Plugin<Project> {
	override fun apply(target: Project) {

		with(target) {
			with(pluginManager) {
				apply("com.android.library")
				apply("org.jetbrains.kotlin.android")
				apply("org.jetbrains.kotlin.plugin.compose")
			}

			extensions.configure<LibraryExtension> {
				defaultConfig.targetSdk = 34
//				configureKotlinAndroid(this)
			}

			extensions.getByType<LibraryExtension>().apply { buildFeatures{compose = true} }

			val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
			dependencies {
				add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
				add("implementation", libs.findLibrary("timber").get())
			}
		}
	}
}