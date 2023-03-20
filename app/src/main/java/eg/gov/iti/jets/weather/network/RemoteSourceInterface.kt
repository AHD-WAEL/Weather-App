package eg.gov.iti.jets.weather.network

import eg.gov.iti.jets.weather.model.Root
import kotlinx.coroutines.flow.Flow

interface RemoteSourceInterface {
    suspend fun getLocation(lat:String, lon:String): Flow<Root>
}