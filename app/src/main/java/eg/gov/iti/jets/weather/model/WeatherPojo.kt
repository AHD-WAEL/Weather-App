package eg.gov.iti.jets.weather.model

import kotlin.collections.ArrayList

data class Alert (
    var sender_name: String,
    var event: String,
    var start: Int,
    var end: Int,
    var description: String,
    var tags: ArrayList<String>,
)

data class Current (
    var dt: Int,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,
    var feels_like: Double,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Double,
    var wind_deg: Int,
    var weather: ArrayList<Weather>
)

data class Daily (
    var dt: Int,
    var sunrise: Int,
    var sunset: Int,
    var moonrise: Int,
    var moonset: Int,
    var moon_phase: Double,
    var temp: Temp,
    var feels_like: FeelsLike,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var wind_speed: Double,
    var wind_deg: Int,
    var wind_gust: Double,
    var weather: ArrayList<Weather>,
    var clouds: Int,
    var pop: Double,
    var uvi: Double,
    var rain: Double,
)

data class FeelsLike (
    var day: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)

data class Hourly (
    var dt: Int,
    var temp: Double,
    var feels_like: Double,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Double,
    var wind_deg: Int,
    var wind_gust: Double,
    var weather: ArrayList<Weather>,
    var pop: Double
)

data class Root (
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var current: Current,
    var hourly: ArrayList<Hourly>,
    var daily: ArrayList<Daily>,
    var alerts: ArrayList<Alert>,
)

data class Temp (
    var day: Double,
    var min: Double,
    var max: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)

data class Weather (
    var id: Int,
    var main: String,
    var description: String,
    var icon: String,
)