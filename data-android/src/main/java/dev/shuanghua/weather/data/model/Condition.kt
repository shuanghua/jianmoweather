package dev.shuanghua.weather.data.model

data class Condition(
    val id: Int,
    val cityId: String,
    val name: String,
    val value: String,
)

val previewCondition = listOf(
    Condition(
        id = 0,
        cityId = "28060159493",
        name = "湿度",
        value = "50%",
    ),
    Condition(
        id = 1,
        cityId = "28060159493",
        name = "气压",
        value = "1015.8hPa",
    ),
    Condition(
        id = 2,
        cityId = "28060159493",
        name = "东北偏东风",
        value = "0.3m/s",
    ),
    Condition(
        id = 3,
        cityId = "28060159493",
        name = "24H降雨量",
        value = "0mm",
    ),
    Condition(
        id = 4,
        cityId = "28060159493",
        name = "1H降雨量",
        value = "0mm",
    ),
    Condition(
        id = 5,
        cityId = "28060159493",
        name = "能见度",
        value = "6.8km",
    )
)