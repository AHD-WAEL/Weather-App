package eg.gov.iti.jets.weather.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import eg.gov.iti.jets.weather.model.*

class DatabaseConverters{
    @TypeConverter
    fun currentToGson(current: Current):String{
        return Gson().toJson(current)
    }

    @TypeConverter
    fun gsonToCurrent(string: String):Current{
        return Gson().fromJson(string, Current::class.java)
    }

    @TypeConverter
    fun weatherToGson(weather: List<Weather>):String{
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun gsonToWeather(string: String):List<Weather>{
        return Gson().fromJson(string, Array<Weather>::class.java).toList()
    }

    @TypeConverter
    fun hourlyToGson(hourly: List<Hourly>):String{
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun gsonToHourly(string: String):List<Hourly>{
        return Gson().fromJson(string, Array<Hourly>::class.java).toList()
    }

    @TypeConverter
    fun dailyToGson(daily: List<Daily>):String{
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun gsonToDaily(string: String):List<Daily>{
        return Gson().fromJson(string, Array<Daily>::class.java).toList()
    }

    @TypeConverter
    fun alertsToGson(alerts: List<Alert>):String{
        return Gson().toJson(alerts)
    }

    @TypeConverter
    fun gsonToAlert(string: String):List<Alert>{
        return Gson().fromJson(string, Array<Alert>::class.java).toList()
    }
}