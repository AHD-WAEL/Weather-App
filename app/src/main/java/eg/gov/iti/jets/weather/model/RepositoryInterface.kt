package eg.gov.iti.jets.weather.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getLocation(lat:String, lon:String, lang:String): Flow<Root>

    fun getRepoHomeRoot(): Flow<Root>
    suspend fun insertRepoHomeRoot(root: Root): Long

    fun getRepoFavoriteLocation(): Flow<List<FavoriteLocation>>
    suspend fun insertRepoFavoriteLocation(favoriteLocation: FavoriteLocation): Long
    suspend fun deleteRepoFavoriteLocation(favoriteLocation: FavoriteLocation)

    fun getRepoAlertLocation(): Flow<List<CurrentAlert>>
    suspend fun insertRepoAlertLocation(currentAlert: CurrentAlert): Long
    suspend fun deleteRepoAlertLocation(currentAlert: CurrentAlert)
}