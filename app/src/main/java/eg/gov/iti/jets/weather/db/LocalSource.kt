package eg.gov.iti.jets.weather.db

import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.HomeRoot
import eg.gov.iti.jets.weather.model.SpecificDay
import eg.gov.iti.jets.weather.model.SpecificTime
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getHomeRoot(): Flow<HomeRoot>
    suspend fun insertHomeRoot(homeRoot: HomeRoot): Long

    fun getSpecificDay(): Flow<List<SpecificDay>>
    suspend fun insertSpecificDay(specificDay: SpecificDay): Long

    fun getSpecificTime(): Flow<List<SpecificTime>>
    suspend fun insertSpecificTime(specificTime: SpecificTime): Long

    fun getFavoriteLocation(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation)
}