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
import dev.shuanghua.weather.data.android.network.model.NetworkModel
import dev.shuanghua.weather.data.android.network.api.Api2
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
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
			cityId = cityId,
			cityName = cityName,
			temperature = t,
			stationName = stationName,
			stationId = obtId,
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
			iconName = it.iconUrl
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

private fun MainWeatherModel.asAlarmIconList(): List<AlarmIcon> {
	var alarmsIconUrl = ""
	return alarmList.mapIndexed { index, alarm ->
		if (alarm.icon != "") {
			alarmsIconUrl = Api2.getImageUrl(alarm.icon)
		}
		AlarmIcon(
			id = index,
			cityId = cityId,
			iconUrl = alarmsIconUrl,
			name = alarm.name
		)
	}
}

private fun MainWeatherModel.asOneHourList(): List<OneHour> {
	return hourList.mapIndexed { index, oneHour ->
		OneHour(
			id = index,
			cityId = cityId,
			hour = oneHour.hour,
			t = oneHour.t,
			icon = oneHour.weatherpic,
			rain = oneHour.rain
		)
	}
}

private fun MainWeatherModel.asOneDayList(): List<OneDay> {
	return dayList.mapIndexed { index, oneDay ->
		OneDay(
			id = index,
			cityId = cityId,
			date = oneDay.date.ifNullToEmpty(),
			week = oneDay.week.ifNullToEmpty(),
			desc = oneDay.desc.ifNullToEmpty(),
			t = "${oneDay.minT}~${oneDay.maxT}",
			minT = oneDay.minT.ifNullToEmpty(),
			maxT = oneDay.maxT.ifNullToEmpty(),
			iconUrl = Api2.getImageUrl(oneDay.wtype.ifNullToEmpty())
		)
	}
}

private fun MainWeatherModel.mapToConditionList(): List<Condition> {
	return element?.let {
		val conditions = ArrayList<Condition>()
		conditions.add(Condition(id = 0, cityId = cityId, name = "湿度", value = it.rh))
		conditions.add(Condition(id = 1, cityId = cityId, name = "气压", value = it.pa))
		conditions.add(Condition(id = 2, cityId = cityId, name = it.wd, value = it.ws))
		conditions.add(Condition(id = 3, cityId = cityId, name = "24H降雨量", value = it.r24h))
		conditions.add(Condition(id = 4, cityId = cityId, name = "1H降雨量", value = it.r01h))
		conditions
	} ?: emptyList()
}


private fun MainWeatherModel.mapToExponentList(): List<Exponent> {
	val healthExponents = ArrayList<Exponent>()
	livingIndex?.apply {
		shushidu?.apply {
			healthExponents += Exponent(
				id = 0,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		gaowen?.apply {
			healthExponents += Exponent(
				id = 1,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		ziwaixian?.apply {
			healthExponents += Exponent(
				id = 2,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		co?.apply {
			healthExponents += Exponent(
				id = 3,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		meibian?.apply {
			healthExponents += Exponent(
				id = 4,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		chenlian?.apply {
			healthExponents += Exponent(
				id = 5,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		luyou?.apply {
			healthExponents += Exponent(
				id = 6,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		liugan?.apply {
			healthExponents += Exponent(
				id = 7,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
		chuanyi?.apply {
			healthExponents += Exponent(
				id = 8,
				cityId = cityId,
				title = title,
				level = level,
				levelDesc = level_desc,
				levelAdvice = level_advice
			)
		}
	}
	return healthExponents
}