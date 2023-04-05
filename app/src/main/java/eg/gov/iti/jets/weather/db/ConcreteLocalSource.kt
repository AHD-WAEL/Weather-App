package eg.gov.iti.jets.weather.db

import android.content.Context
import eg.gov.iti.jets.weather.model.*
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(var context: Context):LocalSource {

    private val homeDao:HomeDao by lazy {
        val homeDB = HomeDB.getInstance(context)
        homeDB.getHomeDB()
    }

    override fun getHomeRoot(): Flow<Root> {
        return homeDao.getHomeRoot()
    }

    override suspend fun insertHomeRoot(root: Root): Long {
        return homeDao.insertHomeRoot(root)
    }

    override fun getFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return homeDao.getFavoriteLocation()
    }

    override suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        return homeDao.insertFavoriteLocation(favoriteLocation)
    }

    override suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation) {
        homeDao.deleteFavoriteLocation(favoriteLocation)
    }

    override fun getAlertLocation(): Flow<List<CurrentAlert>> {
        return homeDao.getAlertLocation()
    }

    override suspend fun insertAlertLocation(currentAlert: CurrentAlert): Long {
        return homeDao.insertAlertLocation(currentAlert)
    }

    override suspend fun deleteAlertLocation(currentAlert: CurrentAlert) {
        homeDao.deleteAlertLocation(currentAlert)
    }
}