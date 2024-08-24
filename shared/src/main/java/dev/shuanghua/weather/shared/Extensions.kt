package dev.shuanghua.weather.shared


fun String?.ifNullToValue(value: String = ""): String {
	return this ?: value
}

fun String?.ifNullToEmpty(): String {
	return this ?: ""
}

fun <T> List<T>?.ifNullToEmpty(): List<T> {
	return this ?: emptyList()
}

