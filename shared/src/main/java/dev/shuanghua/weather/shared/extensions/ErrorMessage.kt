package dev.shuanghua.weather.shared.extensions


fun String?.ifNullToValue(value: String = ""): String {
    return this ?: value
}

fun String.message(): String {
    if(this.contains("Unable to resolve host")) {
        return "连接服务器失败"
    }
    if(this.contains("定位服务没有开启，请在设置中打开定位服务开关")) {
        return "手机定位功能被关闭，请在设置或下拉状态栏中打开定位服务开关"
    }
    if(this.contains("网络连接异常")) {
        return "网络连接异常,请检查网络连接"
    }
    return this
}

fun <T> List<T>?.ifNullToEmpty(): List<T> {
    return this ?: emptyList()
}