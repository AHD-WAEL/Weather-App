package eg.gov.iti.jets.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import eg.gov.iti.jets.weather.Constants
import java.util.*

@Entity(tableName = "SpecificDay")
data class SpecificDay(
    @PrimaryKey
    var specificDaITD: Int,
    var day:String,
    var description:String,
    var highest:Double,
    var lowest:Double,
    var img:String
){
    companion object{
        fun getSpecificDay(root: Root):List<SpecificDay>{
            val week = mutableListOf<SpecificDay>()
            for(i in 1 until root.daily.size){
                val accurateTime = root.daily[i].dt.toLong()*1000
                val date = Date(accurateTime).toString()
                val dayName = date.split(" ")[0]
                val day = SpecificDay(
                    i,
                    dayName,
                    root.daily[i].weather[0].description,
                    root.daily[i].temp.max,
                    root.daily[i].temp.min,
                    root.daily[i].weather[0].icon
                )
                week.add(day)
            }
            return week
        }
    }
}

@Entity(tableName = "SpecificTime")
data class SpecificTime(
    @PrimaryKey
    var specificTimeID: Int,
    var specificTime:String,
    var img:String,
    var temperature:Double
){
    companion object{
        fun getSpecificTime(root:Root):List<SpecificTime>{
            val fullDay = mutableListOf<SpecificTime>()
            for (i in 0 .. 24){
                val accurateTime =(root.hourly[i].dt+root.timezone_offset-7200).toLong()*1000
                val date = Date(accurateTime).toString()
                val time = date.split(" ")[3]
                val hour = time.split(":")[0].toInt()
                val hourString = Constants.getTime(hour)
                val specificTime = SpecificTime(
                    i,
                    hourString,
                    root.hourly[i].weather[0].icon,
                    root.hourly[i].temp
                )
                fullDay.add(specificTime)
            }
            return fullDay
        }
    }
}

@Entity(tableName = "HomeRoot")
data class HomeRoot(
    @PrimaryKey
    var homeID: Int,
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var dt: Int,
    var temp: Double,
    var pressure: Int,
    var humidity: Int,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Double,
    var id: Int,
    var main: String,
    var description: String,
    var icon: String,
){
    companion object{
        fun getHomeRootFromRoot(root: Root): HomeRoot{
            return HomeRoot(
                0,
                root.lat,
                root.lon,
                root.timezone,
                root.timezone_offset,
                root.current.dt,
                root.current.temp,
                root.current.pressure,
                root.current.humidity,
                root.current.uvi,
                root.current.clouds,
                root.current.visibility,
                root.current.wind_speed,
                root.current.weather[0].id,
                root.current.weather[0].main,
                root.current.weather[0].description,
                root.current.weather[0].icon
            )
        }
    }
}

@Entity(tableName = "FavoriteLocation")
data class FavoriteLocation(
    @PrimaryKey
    var name: String,
    var lat: String,
    var lon: String
)