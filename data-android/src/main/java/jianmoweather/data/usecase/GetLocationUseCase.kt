package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.AppCoroutineDispatchers
import com.moshuanghua.jianmoweather.shared.usecase.CoroutineResultUseCase
import jianmoweather.data.repo.location.LocationDataSource
import jianmoweather.data.repo.location.LocationError
import jianmoweather.data.repo.location.LocationSuccess
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
	private val locationDataSource: LocationDataSource,
	dispatcher: AppCoroutineDispatchers,
) : CoroutineResultUseCase<Unit, GetLocationUseCase.Location>(dispatcher) {

	data class Location(
		val cityName: String,
		val latitude: String,
		val longitude: String,
		val district: String,
		val address: String
	)

	override suspend fun execute(parameters: Unit?): Location {
		return when(val result = locationDataSource.getOnceLocationResult()) {
			is LocationSuccess -> {
				result.data.run {
					Location(
						cityName = city,
						latitude = latitude.toString(),
						longitude = longitude.toString(),
						district = district,
						address = address,
					)
				}
			}
			is LocationError -> throw result.throwable
		}
	}
}
