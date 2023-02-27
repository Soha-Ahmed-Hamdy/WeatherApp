package com.example.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root

@Dao
interface favouritePlaceDAO {
    @Query("SELECT * FROM FavouritePlace")
    suspend fun allFavouriteWeather(): MutableList<FavouritePlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favPlace: FavouritePlace?)

    @Delete
    suspend fun deleteFavouritePlace(favPlace: FavouritePlace?)


}