package dev.shuanghua.weather.data.android.location

import android.content.Context
import com.amap.api.location.AMapLocationClient


object AMapPrivacyCheck {
    fun init(context: Context) {
        AMapLocationClient.updatePrivacyShow(context, true, true)
        AMapLocationClient.updatePrivacyAgree(context, true)
    }
}
