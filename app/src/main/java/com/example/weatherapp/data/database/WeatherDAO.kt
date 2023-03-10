package com.example.weatherapp.data.database

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