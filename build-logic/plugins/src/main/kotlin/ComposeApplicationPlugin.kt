import com.android.build.api.dsl.ApplicationExtension
import dev.shuanghua.weather.androidComposeConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * 启用 Compose
 * 为某个模块启用 Compose , 同时还要添加 Compose 依赖
 * 本项目添加 Compose 依赖是依赖 ui-core:core 模块
 */
class ComposeApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            val extension = extensions.getByType<ApplicationExtension>()
            androidComposeConfig(extension)
        }
    }
}