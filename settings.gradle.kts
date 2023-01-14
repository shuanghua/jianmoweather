pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// buildSrc 不需要通过 include 导入
// Android 库只保留 build.gradle androidManifest.xml, src
// project(":bieming-core").projectDir = new File(rootDir,"libraries/core") 设置别名
// rootProject.name = "JianMoWeather"

include(":app")
include(":shared")

include(":data-android:database")
include(":data-android:datastore")
include(":data-android:domain")
include(":data-android:location")
include(":data-android:model")
include(":data-android:network")
include(":data-android:repository")

include(":ui-core:compose")
include(":ui-core:extension")
include(":ui-core:navigation")
include(":ui-core:sample")

include(":ui-screens:weather")
include(":ui-screens:favorites")
include(":ui-screens:more")
include(":ui-screens:province")
include(":ui-screens:city")
include(":ui-screens:station")
include(":ui-screens:district")
include(":ui-screens:settings")
include(":ui-screens:web")