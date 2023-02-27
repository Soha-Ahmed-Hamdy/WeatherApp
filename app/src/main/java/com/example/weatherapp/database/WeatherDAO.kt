package com.example.weatherapp.database

import androidx.room.*
import com.example.weatherapp.model.*

//@Dao
//interface WeatherDAO {
//
//    @Transaction
//    @Query("SELECT * FROM RootData")
//    fun getAllHourly(): List<RootWithCurrentList>
//
//    @Transaction
//    @Query("SELECT * FROM RootData")
//    fun getCurrentData(): List<RootWithCurrent>
//
//    @Transaction
//    @Query("SELECT * FROM RootData")
//    fun getAllDaily(): List<RootWithDailyList>
//
//    @Transaction
//    @Query("SELECT * FROM CurrentData")
//    fun getCurrentWithWeather(): List<CurrentWithWeather>
//
//    @Transaction
//    @Query("SELECT * FROM DailyData")
//    fun getDailyWithWeather(): List<DailyWithWeather>
//
//}