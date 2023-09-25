@file:JvmName("GithubApiTestKt")

package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.shuanghua.weather.data.android.network.api.Api
import dev.shuanghua.weather.data.android.network.api.Api2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


fun getOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor { println("TestResult------>>$it") }
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(logging).build()
}

fun getMoshi(): Moshi = Moshi.Builder().build()

fun getMoshiAdapter(moshi: Moshi): JsonAdapter<Map<String, Any>> = moshi.adapter(
    Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        Any::class.java
    )
)

class ShenZhenApiTest {

    private val okHttpClient: OkHttpClient = getOkHttpClient()

    private val moshi: Moshi = getMoshi()

    @Test
    fun testShenZhenbApi() = runBlocking(Dispatchers.IO) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Api2.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
        val service = retrofit.create(ApiServiceTest::class.java)
        val dsa = MainWeatherRequest()
        val data = service.getMainWeather2(dsa)
        println("------>>TestResult=$data")
    }

    private val mainParams = "lat=22.760415390391916&lon=113.81032715062695&pcity=深圳市&parea=宝安区&rainm=1&uid=d6OIg9m36iZ4kri8sztq"

}