package eg.gov.iti.jets.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object Constants {
    const val baseUrl = "https://api.openweathermap.org/data/2.5/"
    const val apiKey = "b1ae6e2d300c78c2129844e69b0f041d"
    private const val imgURL = "https://openweathermap.org/img/wn/"
    const val settingPreferences = "Setting"
    const val locationPreferences = "Location"
    const val FavPreferences = "FavPref"
    const val currentLocation = "currentLocation"

    fun getImage(position: String): String{
        return "$imgURL$position.png"
    }

    fun getTime(hour: Int): String{
        var time = hour
        val hourString:String
        if(time > 12)
        {
            time -= 12
            hourString = time.toString()+"pm"
        }
        else if(time==12) hourString = time.toString()+"pm"
        //else if(hour==0) hourString = "12am"
        else hourString = time.toString()+"am"
        return hourString
    }

    fun fromCtoF(c: Double): Double = ((c+(9.0/5.0))+32)
    fun fromCtoK(c: Double): Double = (c + 273.15)
    fun fromMStoMH(ms: Double): Double = (ms * 2.237)
    fun iconImage(img:String):Int{
        when (img) {
            "01d" -> return R.drawable.a01d
            "01n" -> return R.drawable.a01n
            "02d" -> return R.drawable.a02d
            "02n" -> return R.drawable.a02n
            "03d" -> return R.drawable.a03d
            "03n" -> return R.drawable.a03n
            "04d" -> return R.drawable.a04d
            "04n" -> return R.drawable.a04n
            "09d" -> return R.drawable.a09d
            "09n" -> return R.drawable.a09n
            "10d" -> return R.drawable.a10d
            "10n" -> return R.drawable.a10n
            "11d" -> return R.drawable.a011d
            "11n" -> return R.drawable.a011n
            "13d" -> return R.drawable.a013d
            "13n" -> return R.drawable.a013n
            "50d" -> return R.drawable.a050d
            else -> return R.drawable.a050n
        }
    }

    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}

