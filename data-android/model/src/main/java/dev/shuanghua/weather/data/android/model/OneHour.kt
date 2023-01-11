package dev.shuanghua.weather.data.android.model

data class OneHour(
    val id: Int,
    val cityId: String,
    val hour: String,
    val t: String,
    val icon: String,
    val rain: String,
)

val previewOneHour = listOf(
    OneHour(
        id = 0,
        cityId = "28060159493",
        hour = "00时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 1,
        cityId = "28060159493",
        hour = "01时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 2,
        cityId = "28060159493",
        hour = "02时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 3,
        cityId = "28060159493",
        hour = "03时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 4,
        cityId = "28060159493",
        hour = "04时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 5,
        cityId = "28060159493",
        hour = "05时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 6,
        cityId = "28060159493",
        hour = "06时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    ),
    OneHour(
        id = 7,
        cityId = "28060159493",
        hour = "07时",
        t = "26°",
        icon = "",
        rain = "无降雨",
    )
)