package com.example.weatherapp.data.database

import androidx.room.*
import com.example.weatherapp.data.model.LocalAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {

    @Query("SELECT * FROM LocalAlert")
    fun allAlerts(): Flow<List<LocalAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: LocalAlert)

    @Delete
    suspend fun deleteAlert(alert: LocalAlert)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAlert(alert: LocalAlert): Long
//
//    @Query("DELETE FROM LocalAlert WHERE id= : id")
//    suspend fun deleteAlert(id: Int)

//    @Query("SELECT * FROM LocalAlert WHERE id= : id")
//    suspend fun getAlert(id: Int):LocalAlert
}