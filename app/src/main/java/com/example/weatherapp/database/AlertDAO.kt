package com.example.weatherapp.database

import androidx.room.*
import com.example.weatherapp.model.LocalAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {

    @Query("SELECT * FROM LocalAlert")
    fun allAlerts(): Flow<List<LocalAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: LocalAlert)

    @Delete
    suspend fun deleteAlert(alert: LocalAlert)
}