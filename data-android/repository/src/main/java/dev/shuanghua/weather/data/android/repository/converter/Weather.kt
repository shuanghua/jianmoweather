package dev.shuanghua.weather.data.android.repository.converter

import dev.shuanghua.weather.data.android.database.entity.AlarmIconEntity
import dev.shuanghua.weather.data.android.database.entity.ConditionEntity
import dev.shuanghua.weather.data.android.database.entity.ExponentEntity
import dev.shuanghua.weather.data.android.database.entity.OneDayEntity
import dev.shuanghua.weather.data.android.database.entity.OneHourEntity
import dev.shuanghua.weather.data.android.database.entity.WeatherEntities
import dev.shuanghua.weather.data.android.database.entity.WeatherEntity
import dev.shuanghua.weather.data.android.model.AlarmIcon
import dev.shuanghua.weather.data.android.model.Condition
import dev.shuanghua.weather.data.android.model.Exponent
import dev.shuanghua.weather.data.android.model.OneDay
import dev.shuanghua.weather.data.android.model.OneHour
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.network.NetworkModel
import dev.shuanghua.weather.data.android.network.api.ShenZhenApi
import dev.shuanghua.weather.data.android.network.model.SzwModel
import dev.shuanghua.weather.shared.ifNullToEmpty

/**
 * 先转成外部模型
 * 然后再转成Entity
 */
fun NetworkModel.asExternalModel(): Weather {
	szw.apply {
		val airQuality = cleanAirQuality()
		val airQualityIcon = cleanAirQualityIcon()
		val lunarCalendar = cleanCalendar()
		val (sunUp, sunDown) = cleanSunTime()
		return Weather(
			cityId = cityid,
			cityName = cityName,
			temperature = t,
			stationName = stationName,
			stationId = obtidyb,
			locationStationId = autoObtid,
			description = desc,
			lunarCalendar = lunarCalendar,
			sunUp = sunUp,
			sunDown = sunDown,
			airQuality = airQuality,
			airQualityIcon = airQualityIcon,
			alarmIcons = asAlarmIconList(),
			oneDays = asOneDayList(),
			oneHours = asOneHourList(),
			conditions = mapToConditionList(),
			exponents = mapToExponentList()
		)
	}
}

fun NetworkModel.toEntities(): WeatherEntities {
	return asExternalModel().toEntities()
}

fun Weather.toEntities(): WeatherEntities {
	return WeatherEntities(
		weatherEntity = asWeatherEntity(),
		alarmEntities = asAlarmEntityList(),
		oneDayEntities = asOneDayEntityList(),
		onHourEntities = asOneHourEntityList(),
		conditionEntities = asConditionEntityList(),
		exponentEntities = asExponentEntityList(),
	)
}

//--------一下部分是每个表的数据--------------------------------------

private fun Weather.asWeatherEntity() = WeatherEntity(
	cityId = cityId,
	cityName = cityName,
	temperature = temperature,
	stationName = stationName,
	stationId = stationId,
	locationStationId = locationStationId,
	description = description,
	lunarCalendar = lunarCalendar,
	sunUp = sunUp,
	sunDown = sunDown,
	airQuality = airQuality,
	airQualityIcon = airQualityIcon,
)


private fun Weather.asAlarmEntityList(): List<AlarmIconEntity> {
	return alarmIcons.map {
		AlarmIconEntity(
			id = it.id,
			cityId = it.cityId,
			iconUrl = it.iconUrl,
			name = it.name
		)
	}
}

private fun Weather.asOneHourEntityList(): List<OneHourEntity> {
	return oneHours.map {
		OneHourEntity(
			id = it.id,
			cityId = it.cityId,
			hour = it.hour,
			t = it.t,
			icon = it.icon,
			rain = it.rain
		)
	}
}

private fun Weather.asOneDayEntityList(): List<OneDayEntity> {
	return oneDays.map {
		OneDayEntity(
			id = it.id,
			cityId = it.cityId,
			date = it.date,
			week = it.week,
			desc = it.desc,
			t = it.t,
			minT = it.minT,
			maxT = it.maxT,
			iconName = it.iconName
		)
	}
}

private fun Weather.asConditionEntityList(): List<ConditionEntity> {
	return conditions.map {
		ConditionEntity(
			id = it.id,
			cityId = it.cityId,
			name = it.name,
			value = it.value
		)
	}
}

/**
 * 健康指数仅支持深圳地区
 */
private fun Weather.asExponentEntityList(): List<ExponentEntity> {
	return exponents.map {
		ExponentEntity(
			id = it.id,
			cityId = it.cityId,
			title = it.title,
			level = it.level,
			levelDesc = it.levelDesc,
			levelAdvice = it.levelAdvice
		)
	}
}

private fun SzwModel.asAlarmIconList(): List<AlarmIcon> {
	var alarmsIconUrl = ""
	return alarmList.mapIndexed { index, alarm ->
		if (alarm.icon != "") {
			alarmsIconUrl = ShenZhenApi.IMAGE_URL + alarm.icon
		}
		AlarmIcon(
			id = index,
			cityId = cityid,
			iconUrl = alarmsIconUrl,
			name = alarm.name
		)
	}
}

private fun SzwModel.asOneHourList(): List<OneHour> {
	return hourForeList.mapIndexed { index, oneHour ->
		OneHour(
			id = index,
			cityId = cityid,
			hour = oneHour.hour,
			t = oneHour.t,
			icon = oneHour.weatherpic,
			rain = oneHour.rain
		)
	}
}

private fun SzwModel.asOneDayList(): List<OneDay> {
	return dayList.mapIndexed { index, oneDay ->
		OneDay(
			id = index,
			cityId = cityid,
			date = oneDay.date.ifNullToEmpty(),
			week = oneDay.week.ifNullToEmpty(),
			desc = oneDay.desc.ifNullToEmpty(),
			t = "${oneDay.minT}~${oneDay.maxT}",
			minT = oneDay.minT.ifNullToEmpty(),
			maxT = oneDay.maxT.ifNullToEmpty(),
			iconName = oneDay.wtype.ifNullToEmpty()
		)
	}
}

private fun SzwModel.mapToConditionList(): List<Condition> {
	val conditions = ArrayList<Condition>()
	if (rh.isNotEmpty()) {
		val rhItem = Condition(
			id = 0,
			cityId = cityid,
			name = "湿度",
			value = rh
		)
		conditions.add(rhItem)
	}
	if (pa.isNotEmpty()) {
		val hPaItem = Condition(
			id = 1,
			cityId = cityid,
			name = "气压",
			value = pa
		)
		conditions.add(hPaItem)
	}
	if (wwCN.isNotEmpty()) {//东风
		val windItem = Condition(
			id = 2,
			cityId = cityid,
			name = wwCN,
			value = wf
		)
		conditions.add(windItem)
	}
	if (r24h.isNotEmpty()) {
		val r24hItem = Condition(
			id = 3,
			cityId = cityid,
			name = "24H降雨量",
			value = r24h
		)
		conditions.add(r24hItem)
	}
	if (r01h.isNotEmpty()) {
		val r01hItem = Condition(
			id = 4,
			cityId = cityid,
			name = "1H降雨量",
			value = r01h
		)
		conditions.add(r01hItem)
	}
	if (v.isNotEmpty()) {
		val visibilityItem = Condition(
			id = 5,
			cityId = cityid,
			name = "能见度",
			value = v
		)
		conditions.add(visibilityItem)
	}
	return conditions
}

private fun SzwModel.mapToExponentList(): List<Exponent> {
	val healthExponents = ArrayList<Exponent>()
	jkzs?.apply {
		shushidu?.apply {
			val healthExponent = Exponent(
				id = 0,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		gaowen?.apply {
			val healthExponent = Exponent(
				id = 1,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		ziwaixian?.apply {
			val healthExponent = Exponent(
				id = 2,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		co?.apply {
			val healthExponent = Exponent(
				id = 3,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		meibian?.apply {
			val healthExponent = Exponent(
				id = 4,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		chenlian?.apply {
			val healthExponent = Exponent(
				id = 5,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		luyou?.apply {
			val healthExponent = Exponent(
				id = 6,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		liugan?.apply {
			val healthExponent = Exponent(
				id = 7,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(healthExponent)
		}
		chuanyi?.apply {
			val exponent = Exponent(
				id = 8,
				cityId = cityid,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
			healthExponents.add(exponent)
		}
	}
	return healthExponents
}