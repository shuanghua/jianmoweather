package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**

id("com.google.devtools.ksp").version("1.7.20-1.0.8")

implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
implementation("com.squareup.okhttp3:okhttp")
implementation("com.squareup.okhttp3:logging-interceptor")

implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
implementation("com.squareup.moshi:moshi:1.14.0")
ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")


 */


class SZApiTestA {

    private val okHttpClient: OkHttpClient = getOkHttpClient()

    private val moshi: Moshi = getMoshi()

    @Test
    fun testGithubApi() = runBlocking(Dispatchers.IO) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val repos: List<Repo?> = service.listRepos("shuanghua")
        println("------>>TestResult=$repos")
    }

}

interface GitHubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String?): List<Repo?>
}

@JsonClass(generateAdapter = true)
data class Repo(
    val id: Int,
    val name: String,
    @Json(name = "html_url") val htmlUrl: String,
    @Json(name = "description") val desc: String?,
)