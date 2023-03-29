package eg.gov.iti.jets.weather.db

import androidx.room.*
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao{
    @Query("Select * From HomeRoot")
    fun getHomeRoot(): Flow<Root>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeRoot(root: Root): Long

    @Query("Select * From FavoriteLocation")
    fun getFavoriteLocation(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long

    @Delete
    suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation)

    @Query("Select * From CurrentAlert")
    fun getAlertLocation(): Flow<List<CurrentAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertLocation(currentAlert: CurrentAlert): Long

    @Delete
    suspend fun deleteAlertLocation(currentAlert: CurrentAlert)
}