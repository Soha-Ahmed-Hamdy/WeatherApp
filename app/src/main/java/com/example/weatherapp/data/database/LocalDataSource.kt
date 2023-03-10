package com.example.weatherapp.data.database

import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.repository.DataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(

    private val alertDao: AlertDAO,
    private val favDao: favouritePlaceDAO,
    private val homeDao: HomeDao

) : DataSource {
    override fun allAlerts(): Flow<List<LocalAlert>> {
        return alertDao.allAlerts()
    }

    override suspend fun insertAlert(alert: LocalAlert) {
        alertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: LocalAlert) {
        alertDao.deleteAlert(alert)
    }

    override fun allFavouriteWeather(): Flow<List<FavouritePlace>> {
        return favDao.allFavouriteWeather()
    }

    override suspend fun insertFavourite(favPlace: FavouritePlace?) {
        favDao.insertFavourite(favPlace)
    }

    override suspend fun deleteFavouritePlace(favPlace: FavouritePlace?) {
        favDao.deleteFavouritePlace(favPlace)
    }

    override fun allHomeWeatherData(): Flow<Root> {
        return  homeDao.allHomeWeatherData()
    }

    override suspend fun deleteRootData() {
        homeDao.deleteRootData()
    }

    override suspend fun insertRoot(root: Root) {
        homeDao.insertRoot(root)
    }

    override suspend fun getRoot(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        TODO("Not yet implemented")
    }


}