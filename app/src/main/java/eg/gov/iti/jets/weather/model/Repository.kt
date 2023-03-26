package eg.gov.iti.jets.weather.model

import eg.gov.iti.jets.weather.db.LocalSource
import eg.gov.iti.jets.weather.network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow

class Repository(private var remoteSourceInterface: RemoteSourceInterface, private var localSource: LocalSource):RepositoryInterface {
    override suspend fun getLocation(lat: String, lon: String, lang: String): Flow<Root> {
        return remoteSourceInterface.getLocation(lat, lon, lang)
    }

    override fun getRepoHomeRoot(): Flow<HomeRoot> {
        return localSource.getHomeRoot()
    }

    override suspend fun insertRepoHomeRoot(homeRoot: HomeRoot): Long {
        return localSource.insertHomeRoot(homeRoot)
    }

    override fun getRepoSpecificDay(): Flow<List<SpecificDay>> {
        return localSource.getSpecificDay()
    }

    override suspend fun insertRepoSpecificDay(specificDay: SpecificDay): Long {
        return localSource.insertSpecificDay(specificDay)
    }

    override fun getRepoSpecificTime(): Flow<List<SpecificTime>> {
        return localSource.getSpecificTime()
    }

    override suspend fun insertRepoSpecificTime(specificTime: SpecificTime): Long {
        return localSource.insertSpecificTime(specificTime)
    }

    override fun getRepoFavoriteLocation(): Flow<List<FavoriteLocation>> {
        return localSource.getFavoriteLocation()
    }

    override suspend fun insertRepoFavoriteLocation(favoriteLocation: FavoriteLocation): Long {
        return localSource.insertFavoriteLocation(favoriteLocation)
    }

    override suspend fun deleteRepoFavoriteLocation(favoriteLocation: FavoriteLocation) {
        return localSource.deleteFavoriteLocation(favoriteLocation)
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