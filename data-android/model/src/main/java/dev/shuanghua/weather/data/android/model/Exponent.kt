package dev.shuanghua.weather.data.android.model

data class Exponent(
    val id: Int,
    val cityId: String,
    val title: String,
    val level: String,
    val levelDesc: String,
    val levelAdvice: String,
)

val previewExponent = listOf(
    Exponent(
        id = 0,
        cityId = "28060159493",
        title = "舒适度指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 1,
        cityId = "28060159493",
        title = "穿衣指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 2,
        cityId = "28060159493",
        title = "旅游指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 3,
        cityId = "28060159493",
        title = "高温热浪指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 4,
        cityId = "28060159493",
        title = "紫外线指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 5,
        cityId = "28060159493",
        title = "一氧化碳指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 6,
        cityId = "28060159493",
        title = "霉变指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    ),
    Exponent(
        id = 7,
        cityId = "28060159493",
        title = "晨练指数",
        level = "",
        levelDesc = "舒服",
        levelAdvice = "",
    )
)