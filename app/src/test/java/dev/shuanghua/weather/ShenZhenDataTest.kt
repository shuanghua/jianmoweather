package dev.shuanghua.weather

import dev.shuanghua.weather.data.network.ShenZhenService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ShenZhenDataTest {

    @Test
    fun testCheckShenZhenWeatherData() = runBlocking {
        val data =
            "{\"pcity\":\"\",\"parea\":\"\",\"lon\":\"\",\"lat\":\"\",\"uid\":\"860505026439122sztq\",\"os\":\"android19\",\"ver\":\"v5.1.4\",\"net\":\"WIFI\",\"type\":\"1\",\"token\":\"0669bfe7df2b3d2044ff8c23860b698a2ec1bdf5\",\"rever\":\"514\",\"uname\":\"\",\"Param\":{\"cityid\":\"\",\"cityids\":\"28060159493,32010145005,28010159287,02010058362,01010054511\",\"isauto\":\"1\",\"obtid\":\"\",\"h\":\"720\",\"w\":\"1184\"}}"
        val shenZhenService = Retrofit.Builder()
            .baseUrl(ShenZhenService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ShenZhenService::class.java)
        val result = shenZhenService.getWeather(data)
        print("JianMoTest:----> $result")
    }
}