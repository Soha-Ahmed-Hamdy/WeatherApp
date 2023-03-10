package com.example.weatherapp.data.database

import androidx.room.*
import com.example.weatherapp.data.model.FavouritePlace
import kotlinx.coroutines.flow.Flow

@Dao
interface favouritePlaceDAO {
    @Query("SELECT * FROM FavouritePlace")
    fun allFavouriteWeather(): Flow<List<FavouritePlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favPlace: FavouritePlace?)

    @Delete
    suspend fun deleteFavouritePlace(favPlace: FavouritePlace?)


}