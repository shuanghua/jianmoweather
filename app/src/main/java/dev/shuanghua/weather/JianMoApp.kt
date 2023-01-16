package dev.shuanghua.weather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.shuanghua.weather.data.android.location.AMapPrivacyCheck
import timber.log.Timber

@HiltAndroidApp
class JianMoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AMapPrivacyCheck.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}