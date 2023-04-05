package eg.gov.iti.jets.weather.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository:RepositoryInterface {

    val rootList: MutableList<Root> = arrayListOf()
    val alertList: MutableList<CurrentAlert> = arrayListOf()
    val favList: MutableList<FavoriteLocation> = arrayListOf()


    override suspend fun getLocation(lat: String, lon: String, lang: String): Flow<Root> {
        TODO("Not yet implemented")
    }

    override fun getRepoHomeRoot(): Flow<Root> {
        return flowOf(rootList[0])
    }

    override suspend fun insertRepoHomeRoot(root: Root): Long {
        rootList.add(root)
        return 1
    }

    override fun getRepoFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return flowOf(favList)
    }

    override suspend fun insertRepoFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        favList.add(favoriteLocation)
        return 1
    }

    override suspend fun deleteRepoFavoriteLocation(favoriteLocation: FavoriteLocation) {
        favList.remove(favoriteLocation)
    }

    override fun getRepoAlertLocation(): Flow<List<CurrentAlert>> {
        return flowOf(alertList)
    }

    override suspend fun insertRepoAlertLocation(currentAlert: CurrentAlert): Long {
        alertList.add(currentAlert)
        return 1
    }

    override suspend fun deleteRepoAlertLocation(currentAlert: CurrentAlert) {
        alertList.remove(currentAlert)
    }
}