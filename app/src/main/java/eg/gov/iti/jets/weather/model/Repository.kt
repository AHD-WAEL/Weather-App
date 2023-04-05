package eg.gov.iti.jets.weather.model

import eg.gov.iti.jets.weather.db.LocalSource
import eg.gov.iti.jets.weather.network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow

class Repository private constructor(private var remoteSourceInterface: RemoteSourceInterface, private var localSource: LocalSource):RepositoryInterface {
    override suspend fun getLocation(lat: String, lon: String, lang: String): Flow<Root> {
        return remoteSourceInterface.getLocation(lat, lon, lang)
    }

    override fun getRepoHomeRoot(): Flow<Root> {
        return localSource.getHomeRoot()
    }

    override suspend fun insertRepoHomeRoot(root: Root): Long {
        return localSource.insertHomeRoot(root)
    }

    override fun getRepoFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return localSource.getFavoriteLocation()
    }

    override suspend fun insertRepoFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        return localSource.insertFavoriteLocation(favoriteLocation)
    }

    override suspend fun deleteRepoFavoriteLocation(favoriteLocation: FavoriteLocation) {
        localSource.deleteFavoriteLocation(favoriteLocation)
    }

    override fun getRepoAlertLocation(): Flow<List<CurrentAlert>> {
        return localSource.getAlertLocation()
    }

    override suspend fun insertRepoAlertLocation(currentAlert: CurrentAlert): Long {
        return localSource.insertAlertLocation(currentAlert)
    }

    override suspend fun deleteRepoAlertLocation(currentAlert: CurrentAlert) {
        localSource.deleteAlertLocation(currentAlert)
    }

    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSourceInterface: RemoteSourceInterface, localSource: LocalSource): Repository{
            return instance?: synchronized(this){
                val repo = Repository(remoteSourceInterface, localSource)
                instance = repo
                repo
            }
        }
    }
}