package eg.gov.iti.jets.weather.model

import eg.gov.iti.jets.weather.network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow

class Repository(private var remoteSourceInterface: RemoteSourceInterface):RepositoryInterface {
    override suspend fun getLocation(lat: String, lon: String): Flow<Root> {
        return remoteSourceInterface.getLocation(lat, lon)
    }

    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSourceInterface: RemoteSourceInterface): Repository{
            return instance?: synchronized(this){
                val repo = Repository(remoteSourceInterface)
                instance = repo
                repo
            }
        }
    }
}