package dev.shuanghua.weather.data.model

data class OneDay(
    val id: Int,
    val cityId: String,
    val date: String,//日期
    val week: String,//今天
    val desc: String,//描述
    val t: String,     //天气范围
    val minT: String,//最低温度
    val maxT: String,//最高温度
    val iconName: String,//相应天气 icon 名字
)

val previewOnDay = listOf(
    OneDay(
        id = 0,
        cityId = "28060159493",
        date = "12/22",
        week = "明日",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 1,
        cityId = "28060159493",
        date = "12/23",
        week = "星期一",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 2,
        cityId = "28060159493",
        date = "12/24",
        week = "星期二",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 3,
        cityId = "28060159493",
        date = "12/25",
        week = "星期三",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 4,
        cityId = "28060159493",
        date = "12/26",
        week = "星期四",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 5,
        cityId = "28060159493",
        date = "12/27",
        week = "星期五",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 6,
        cityId = "28060159493",
        date = "12/28",
        week = "星期六",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    ),
    OneDay(
        id = 7,
        cityId = "28060159493",
        date = "12/29",
        week = "星期日",
        desc = "晴天干燥",
        t = "15°",
        minT = "13°",
        maxT = "20°",
        iconName = ""
    )
)