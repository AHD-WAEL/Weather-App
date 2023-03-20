package eg.gov.iti.jets.weather.model

import eg.gov.iti.jets.weather.Constants
import java.util.*

data class SpecificDay(
    var day:String,
    var description:String,
    var highest:Double,
    var lowest:Double,
    var img:String
){
    companion object{
        fun getSpecificDay(root: Root):List<SpecificDay>{
            val week = mutableListOf<SpecificDay>()
            for(i in root.daily.indices){
                val accurateTime = root.daily[i].dt.toLong()*1000
                val date = Date(accurateTime).toString()
                val dayName = date.split(" ")[0]
                val day = SpecificDay(
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

data class SpecificTime(
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

