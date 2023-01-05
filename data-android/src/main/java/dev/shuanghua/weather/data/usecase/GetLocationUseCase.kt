package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.CoroutineResultUseCase
import dev.shuanghua.weather.data.repo.NetworkLocationDataSource
import dev.shuanghua.weather.data.repo.LocationError
import dev.shuanghua.weather.data.repo.LocationSuccess
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationDataSource: NetworkLocationDataSource,
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
