package dev.shuanghua.weather.data.android.network.params

import dev.shuanghua.weather.data.android.model.OuterParams

fun OuterParams.toMap(): MutableMap<String, Any> = mutableMapOf(
    "type" to type,
    "ver" to ver,
    "rever" to rever,
    "net" to net,
    "pcity" to pcity,
    "parea" to parea,
    "lon" to lon,
    "lat" to lat,
    "gif" to gif,
    "uid" to uid,
    "uname" to uname,
    "token" to token,
    "os" to os
)
