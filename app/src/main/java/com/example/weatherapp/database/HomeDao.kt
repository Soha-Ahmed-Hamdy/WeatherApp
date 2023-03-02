package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {

    @Query("SELECT * FROM Root")
    fun allHomeWeatherData(): Flow<Root>

    @Query("DELETE FROM Root")
    suspend fun deleteRootData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoot(root: Root)
}