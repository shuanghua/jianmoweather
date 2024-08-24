package dev.shuanghua.weather.data.android.domain

import dev.shuanghua.weather.data.android.repository.LocationRepository
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

class UpdateDistrictUseCase(
	private val dispatchers: AppDispatchers,
	private val stationRepository: StationRepository,
	private val locationRepository: LocationRepository,
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

	data class Params(val cityId: String)

	override suspend fun doWork(params: Params): Unit = withContext(dispatchers.io) {
		val location = locationRepository.getLocationFromDataStore()
		val paramsMap = createStationParamsMap(params.cityId, location.latitude, location.longitude)
		stationRepository.updateDistrictStationData(paramsMap)
	}

	private fun createStationParamsMap(
		cityId: String,
		lat: String,
		lon: String,
	) = mapOf(
		"cityId" to cityId,
		"obtid" to "",
		"lat" to lat.ifEmpty { "0" },
		"lon" to lon.ifEmpty { "0" },
		"uid" to "MV14fND4imK31h2Hsztq"
	)
}