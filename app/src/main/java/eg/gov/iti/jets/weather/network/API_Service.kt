package eg.gov.iti.jets.weather.network

import eg.gov.iti.jets.weather.model.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface API_Service {
    @GET("onecall")
    suspend fun getLocationFromNetwork(@Query("lat")lat:String, @Query("lon")lon:String, @Query("appid")appid:String,@Query("lang")lang:String, @Query("units")units:String):Root
}