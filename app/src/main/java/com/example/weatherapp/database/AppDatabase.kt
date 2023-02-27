package com.example.weatherapp.database

import android.content.Context
import androidx.room.*
import com.example.weatherapp.model.*



@Database(entities = [Root::class
//    ,CurrentData::class
    ,FavouritePlace::class
//    ,DailyData::class
//    ,WeatherData::class
//    ,TempData::class
                     ],version = 1)

@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouritePlaceDAO(): favouritePlaceDAO?
    abstract fun homeDAO(): HomeDao?

    companion object {
        private var INSTANCE: AppDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "fav_country_database")
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}
