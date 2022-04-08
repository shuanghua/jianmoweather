package com.moshuanghua.jianmoweather

import android.app.Application
import android.content.SharedPreferences
import com.amap.api.location.AMapLocationClient
import dagger.hilt.android.HiltAndroidApp
import jianmoweather.data.repo.city.CityRepository
import jianmoweather.data.repo.province.ProvinceRepository
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class JianMoApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false) // leakcanary

        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this,true)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}