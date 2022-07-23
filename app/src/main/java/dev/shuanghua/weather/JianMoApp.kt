package dev.shuanghua.weather

import android.app.Application
import com.amap.api.location.AMapLocationClient
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JianMoApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false) // leakcanary
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}