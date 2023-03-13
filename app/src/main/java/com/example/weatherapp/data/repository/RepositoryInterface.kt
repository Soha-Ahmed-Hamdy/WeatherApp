package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    fun getStates()

    suspend fun getFavDetails(lat: Double, long: Double): Flow<Root?>

    suspend fun getHomeData(): Flow<Root>?

    suspend fun insertFavCountry(favPlace: FavouritePlace)

    suspend fun deleteFavCountry(favPlace: FavouritePlace)
    fun getAllFavCountry(): Flow<List<FavouritePlace>>

    suspend fun insertAlert(alert: LocalAlert)

    suspend fun deleteAlert(alert: LocalAlert)
    fun getAllAlerts(): Flow<List<LocalAlert>>
    fun checkForInternet(): Boolean
}