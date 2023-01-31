import com.android.build.gradle.LibraryExtension
import dev.shuanghua.weather.androidComposeConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * AndroidLibrary + Hilt + Compose + Navigation + Data
 */
class ScreenPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            pluginManager.apply {
                apply("app.android.library")// library + kotlin + coroutines
                apply("app.android.hilt")   // hilt + kapt
            }
            val extension = extensions.getByType<LibraryExtension>()
            androidComposeConfig(extension)
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", project(":shared"))

                add("implementation", project(":ui-core:compose"))
                add("implementation", project(":ui-core:components"))

                add("implementation", project(":data-android:domain"))
                add("implementation", project(":data-android:model"))
                add("implementation", project(":data-android:repository"))

                add("implementation", libs.findLibrary("hilt.compose.navigation").get())
            }
        }
    }
}