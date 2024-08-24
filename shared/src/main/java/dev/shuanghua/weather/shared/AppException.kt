package dev.shuanghua.weather.shared

fun Exception.appException(): Exception {
	return when {
		this.message?.contains("Unable to resolve host") == true -> Exception("网络连接异常, 请检查网络连接")
		this.message?.contains("定位服务没有开启，请在设置中打开定位服务开关") == true -> Exception("定位服务没有开启，请在设置中打开定位服务开关")
		this.message?.contains("网络连接异常") == true -> Exception("网络连接异常, 请检查网络连接")
		else -> this
	}
}