package dev.shuanghua.weather.inject

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shuanghua.weather.data.repo.location.LocationDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationDataSource(
        client: AMapLocationClient
    ) = LocationDataSource(client)

    @Singleton
    @Provides
    fun provideAMapLocationClient(
        @ApplicationContext appContext: Context,
        aMapOption: AMapLocationClientOption
    ) = AMapLocationClient(
        appContext.applicationContext
    ).apply { setLocationOption(aMapOption) }

    @Singleton
    @Provides
    fun provideAMapLocationOption(): AMapLocationClientOption {
        return AMapLocationClientOption().apply {
            httpTimeOut = 3000
            isNeedAddress = true //详细地址
            isOnceLocation = true
            isOnceLocationLatest = true //获取最近3s内精度最高的"一次"定位结果
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        }
    }
}