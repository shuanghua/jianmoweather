package dev.shuanghua.weather.data.usecase

import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserverFavoriteCityIdsUseCase @Inject constructor(
    private val favoriteDao: FavoriteDao,
): ObservableUseCase<String,List<String>>(){
    override fun createObservable(params: String): Flow<List<String>> {
        return favoriteDao.observerFavoriteId()
    }
}