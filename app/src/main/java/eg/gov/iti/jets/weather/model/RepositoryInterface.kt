package eg.gov.iti.jets.weather.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getLocation(lat:String, lon:String, lang:String): Flow<Root>

    fun getRepoHomeRoot(): Flow<HomeRoot>
    suspend fun insertRepoHomeRoot(homeRoot: HomeRoot): Long

    fun getRepoSpecificDay(): Flow<List<SpecificDay>>
    suspend fun insertRepoSpecificDay(specificDay: SpecificDay): Long

    fun getRepoSpecificTime(): Flow<List<SpecificTime>>
    suspend fun insertRepoSpecificTime(specificTime: SpecificTime): Long

    fun getRepoFavoriteLocation(): Flow<List<FavoriteLocation>>
    suspend fun insertRepoFavoriteLocation(favoriteLocation: FavoriteLocation): Long
    suspend fun deleteRepoFavoriteLocation(favoriteLocation: FavoriteLocation)
}