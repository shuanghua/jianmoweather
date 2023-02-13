@file:JvmName("GithubApiTestKt")

package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.shuanghua.weather.data.android.network.api.Api

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
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
        val service = retrofit.create(ApiServiceTest::class.java)
        val data = service.getMainWeather(mainParams)
        println("------>>TestResult=$data")
    }

    private val mainParams =
        "{\"type\":\"1\",\"ver\":\"v5.7.3\",\"rever\":\"586\",\"net\":\"WIFI\",\"pcity\":\"深圳市\",\"parea\":\"宝安区\",\"lon\":\"113.8103331707177\",\"lat\":\"22.76034842246787\",\"gif\":\"true\",\"uid\":\"87Q7RXZz9lX21H8msztqsztq\",\"uname\":\"\",\"token\":\"\",\"os\":\"android33\",\"Param\":{\"cityid\":\"30120659033\",\"isauto\":\"0\",\"w\":1080,\"h\":1083,\"cityids\":\"\",\"pcity\":\"深圳市\",\"parea\":\"宝安区\",\"lon\":\"113.8103331707177\",\"lat\":\"22.76034842246787\",\"gif\":\"true\"}}"
}