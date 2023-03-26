package eg.gov.iti.jets.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eg.gov.iti.jets.weather.model.FavoriteLocation
import eg.gov.iti.jets.weather.model.HomeRoot
import eg.gov.iti.jets.weather.model.SpecificDay
import eg.gov.iti.jets.weather.model.SpecificTime

@Database(entities = arrayOf(HomeRoot::class), version = 1)
abstract class HomeDB:RoomDatabase(){
    abstract fun getHomeDB(): HomeDao
    companion object {
        @Volatile
        private var instance:HomeDB? = null
        fun getInstance(context: Context): HomeDB{
            return instance?: synchronized(this){
                val temp = Room.databaseBuilder(context, HomeDB::class.java,"HomeRoot").build()
                instance = temp
                temp
            }
        }
    }
}

@Database(entities = arrayOf(SpecificDay::class), version = 1)
abstract class DayDB:RoomDatabase(){
    abstract fun getDayDB(): DayDao
    companion object{
        @Volatile
        private var instance:DayDB? = null
        fun getInstance(context: Context): DayDB{
            return instance?: synchronized(this){
                val temp = Room.databaseBuilder(context, DayDB::class.java, "SpecificDay").build()
                instance = temp
                temp
            }
        }
    }
}

@Database(entities = arrayOf(SpecificTime::class), version = 1)
abstract class TimeDB:RoomDatabase(){
    abstract fun getTimeDB(): HourDao
    companion object{
        @Volatile
        private var instance:TimeDB? = null
        fun getInstance(context: Context): TimeDB{
            return instance?: synchronized(this){
                val temp = Room.databaseBuilder(context, TimeDB::class.java, "SpecificTime").build()
                instance = temp
                temp
            }
        }
    }
}

@Database(entities = arrayOf(FavoriteLocation::class), version = 1)
abstract class FavoriteLocationDB:RoomDatabase(){
    abstract fun getFavoriteLocation(): FavouriteDao
    companion object{
        @Volatile
        private var instance:FavoriteLocationDB? = null
        fun getInstance(context: Context): FavoriteLocationDB{
            return instance?: synchronized(this){
                val temp = Room.databaseBuilder(context, FavoriteLocationDB::class.java, "FavoriteLocation").build()
                instance = temp
                temp
            }
        }
    }
}