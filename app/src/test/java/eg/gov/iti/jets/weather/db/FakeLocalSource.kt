package eg.gov.iti.jets.weather.db

import eg.gov.iti.jets.weather.model.CurrentAlert
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSource: LocalSource {

    var rootList: MutableList<Root> = arrayListOf()
    var alertList: MutableList<CurrentAlert> = arrayListOf()
    var favList: MutableList<FavoriteLocation> = arrayListOf()

    override fun getHomeRoot(): Flow<Root> {
        return flowOf(rootList[0])
    }

    override suspend fun insertHomeRoot(root: Root): Long {
        rootList.add(root)
        return 1
    }

    override fun getFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return flowOf(favList)
    }

    override suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        favList.add(favoriteLocation)
        return 1
    }

    override suspend fun deleteFavoriteLocation(favoriteLocation: FavoriteLocation) {
        favList.remove(favoriteLocation)
    }

    override fun getAlertLocation(): Flow<List<CurrentAlert>> {
        return flowOf(alertList)
    }

    override suspend fun insertAlertLocation(currentAlert: CurrentAlert): Long {
        alertList.add(currentAlert)
        return 1
    }

    override suspend fun deleteAlertLocation(currentAlert: CurrentAlert) {
        alertList.remove(currentAlert)
    }
}