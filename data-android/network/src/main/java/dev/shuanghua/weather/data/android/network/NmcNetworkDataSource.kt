package dev.shuanghua.weather.data.android.network

/**
 * 中央气象台数据
 */
interface NmcNetworkDataSource {
	fun getWeather(): String
}

class NmcNetworkDataSourceImpl(
	// api
):NmcNetworkDataSource{
	override fun getWeather(): String {
		TODO("Not yet implemented")
	}

}