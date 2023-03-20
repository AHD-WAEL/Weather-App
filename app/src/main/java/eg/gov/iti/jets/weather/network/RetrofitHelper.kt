package eg.gov.iti.jets.weather.network

import eg.gov.iti.jets.weather.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getInstance(): Retrofit{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.baseUrl).build()
    }
}