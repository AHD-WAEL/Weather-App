package eg.gov.iti.jets.weather.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getLocation(lat:String, lon:String, lang:String): Flow<Root>
}