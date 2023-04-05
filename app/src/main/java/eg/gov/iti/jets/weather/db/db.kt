package eg.gov.iti.jets.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eg.gov.iti.jets.weather.model.*

@Database(entities = arrayOf(Root::class, FavoriteLocation::class, CurrentAlert::class), version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class HomeDB:RoomDatabase(){
    abstract fun getHomeDB(): HomeDao
    companion object {
        @Volatile
        private var instance:HomeDB? = null
        fun getInstance(context: Context): HomeDB{
            return instance?: synchronized(this){
                val temp = Room.databaseBuilder(context, HomeDB::class.java,"RootDB").build()
                instance = temp
                temp
            }
        }
    }
}