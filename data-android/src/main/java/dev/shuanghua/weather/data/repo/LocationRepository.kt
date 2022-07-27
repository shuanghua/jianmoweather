package dev.shuanghua.weather.data.repo

import com.amap.api.location.AMapLocation

class LocationRepository(private val locationDataSource: LocationDataSource) {

	data class Location(
		val cityName: String,
		val latitude: String,
		val longitude: String,
		val district: String,
		val address: String
	)

	suspend fun getLocation(): Location {
		return when(val result = locationDataSource.getOnceLocationResult()) {
			is LocationSuccess -> newLocation(result.data)
			is LocationError -> throw result.throwable
		}
	}

	private fun newLocation(aMapLocation: AMapLocation): Location {
		return aMapLocation.run {
			Location(
				cityName = city,
				latitude = latitude.toString(),
				longitude = longitude.toString(),
				district = district,
				address = address,
			)
		}
	}

	companion object {
		@Volatile
		private var INSTANCE: LocationRepository? = null

		fun getInstance(
            locationDataSource: LocationDataSource,
		): LocationRepository {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: LocationRepository(locationDataSource).also {
					INSTANCE = it
				}
			}
		}
	}
}