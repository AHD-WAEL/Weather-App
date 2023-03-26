package eg.gov.iti.jets.weather.db

import androidx.room.*
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.HomeRoot
import eg.gov.iti.jets.weather.model.SpecificDay
import eg.gov.iti.jets.weather.model.SpecificTime
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao{
    @Query("Select * From HomeRoot")
    fun getHomeRoot(): Flow<HomeRoot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeRoot(homeRoot: HomeRoot): Long
}

@Dao
interface DayDao{
    @Query("Select * From SpecificDay")
    fun getSpecificDay(): Flow<List<SpecificDay>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecificDay(specificDay: SpecificDay): Long
}

@Dao
interface HourDao{
    @Query("Select * From SpecificTime")
    fun getSpecificTime(): Flow<List<SpecificTime>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecificTime(specificTime: SpecificTime): Long
}

@Dao
interface FavouriteDao{
    @Query("Select * From FavoriteLocation")
    fun getFavoriteLocation(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long

    @Delete
    suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation)
}