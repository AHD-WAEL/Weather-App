package eg.gov.iti.jets.weather.network

import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.model.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherClient : RemoteSourceInterface{
    private val apiService: API_Service by lazy {
        RetrofitHelper.getInstance().create(API_Service::class.java)
    }

    override suspend fun getLocation(lat: String, lon: String, lang:String): Flow<Root> {
        return flowOf(apiService.getLocationFromNetwork(lat, lon, Constants.apiKey,lang,"metric"))
    }

    companion object {
        private var instance: WeatherClient? = null

        fun getInstance():WeatherClient{
            return instance?: synchronized(this){
                val client = WeatherClient()
                instance = client
                client
            }
        }
    }
}