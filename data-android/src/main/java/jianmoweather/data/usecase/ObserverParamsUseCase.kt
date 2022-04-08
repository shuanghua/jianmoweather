package jianmoweather.data.usecase

import com.moshuanghua.jianmoweather.shared.usecase.ObservableUseCase
import jianmoweather.data.db.entity.Params
import jianmoweather.data.repo.location.ParamsRepository
import jianmoweather.data.repo.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserverParamsUseCase @Inject constructor(
    val paramsRepository: ParamsRepository
) : ObservableUseCase<Boolean, Params?>() {

    data class Params(val isLocation: Boolean)

    override fun createObservable(params: Boolean): Flow<jianmoweather.data.db.entity.Params?> {
        return if(params) {
            paramsRepository.observerRequestParams(1)
        } else {
            paramsRepository.observerRequestParams(0)
        }
    }
}