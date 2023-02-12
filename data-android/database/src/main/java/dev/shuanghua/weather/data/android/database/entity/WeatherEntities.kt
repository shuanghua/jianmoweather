package dev.shuanghua.weather.data.android.database.entity

data class WeatherEntities(
	val weatherEntity: WeatherEntity,
	val alarmEntities: List<AlarmIconEntity>,
	val oneDayEntities: List<OneDayEntity>,
	val onHourEntities: List<OneHourEntity>,
	val conditionEntities: List<ConditionEntity>,
	val exponentEntities: List<ExponentEntity>
)