package eg.gov.iti.jets.weather.db

import android.content.Context
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.HomeRoot
import eg.gov.iti.jets.weather.model.SpecificDay
import eg.gov.iti.jets.weather.model.SpecificTime
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(var context: Context):LocalSource {

    private val homeDao:HomeDao by lazy {
        val homeDB = HomeDB.getInstance(context)
        homeDB.getHomeDB()
    }

    private val dayDao: DayDao by lazy {
        val dayDB = DayDB.getInstance(context)
        dayDB.getDayDB()
    }

    private val hourDao: HourDao by lazy {
        val hourDB = TimeDB.getInstance(context)
        hourDB.getTimeDB()
    }

    private val favouriteDao: FavouriteDao by lazy {
        val favouriteDB = FavoriteLocationDB.getInstance(context)
        favouriteDB.getFavoriteLocation()
    }

    override fun getHomeRoot(): Flow<HomeRoot> {
        return homeDao.getHomeRoot()
    }

    override suspend fun insertHomeRoot(homeRoot: HomeRoot): Long {
        return homeDao.insertHomeRoot(homeRoot)
    }

    override fun getSpecificDay(): Flow<List<SpecificDay>> {
        return dayDao.getSpecificDay()
    }

    override suspend fun insertSpecificDay(specificDay: SpecificDay): Long {
        return dayDao.insertSpecificDay(specificDay)
    }

    override fun getSpecificTime(): Flow<List<SpecificTime>> {
        return hourDao.getSpecificTime()
    }

    override suspend fun insertSpecificTime(specificTime: SpecificTime): Long {
        return hourDao.insertSpecificTime(specificTime)
    }

    override fun getFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return favouriteDao.getFavoriteLocation()
    }

    override suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        return favouriteDao.insertFavoriteLocation(favoriteLocation)
    }

    override suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation) {
        return favouriteDao.deleteFavoriteLocation(favoriteLocation)
    }
}