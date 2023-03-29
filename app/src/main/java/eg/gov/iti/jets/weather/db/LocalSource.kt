package eg.gov.iti.jets.weather.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getHomeRoot(): Flow<Root>
    suspend fun insertHomeRoot(root: Root): Long

    fun getFavoriteLocation(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation)

    fun getAlertLocation(): Flow<List<CurrentAlert>>
    suspend fun insertAlertLocation(currentAlert: CurrentAlert): Long
    suspend fun deleteAlertLocation(currentAlert: CurrentAlert)
}