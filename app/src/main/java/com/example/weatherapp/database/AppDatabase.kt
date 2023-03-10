package com.example.weatherapp.database

import android.content.Context
import androidx.room.*
import com.example.weatherapp.model.*



@Database(entities = [
    Root::class
    ,FavouritePlace::class
    ,LocalAlert::class
                     ]
    ,version = 1,
exportSchema = false)

@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouritePlaceDAO(): favouritePlaceDAO?
    abstract fun homeDAO(): HomeDao?
    abstract fun alertDAO(): AlertDAO?

    companion object {
        private var INSTANCE: AppDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "fav_country_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}
