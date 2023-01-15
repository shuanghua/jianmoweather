package dev.shuanghua.weather.shared

fun List<Any>.ifEmptyHandle(
    emptyHandle: () -> Unit,
    notEmptyHandle: () -> Unit
) {
    if (isEmpty()) emptyHandle() else notEmptyHandle()
}
