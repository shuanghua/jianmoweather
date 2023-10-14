package dev.shuanghua.weather.data.android.location.di

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import dev.shuanghua.weather.data.android.location.NetworkLocationDataSource
import org.koin.dsl.module


val locationModule = module {
	single<AMapLocationClientOption> {
		AMapLocationClientOption().apply {
			httpTimeOut = 3000
			isNeedAddress = true //详细地址
			isOnceLocation = true
			isOnceLocationLatest = true //获取最近3s内精度最高的"一次"定位结果
			locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
		}
	}

	single<AMapLocationClient> {
		AMapLocationClient(get()).apply { setLocationOption(get()) }
	}

	single { NetworkLocationDataSource(get()) }

}