package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.repository.DataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(
    private val api: Api,
) : DataSource {
    override fun allAlerts(): Flow<List<LocalAlert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: LocalAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: LocalAlert) {
        TODO("Not yet implemented")
    }

    override fun allFavouriteWeather(): Flow<List<FavouritePlace>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavourite(favPlace: FavouritePlace?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouritePlace(favPlace: FavouritePlace?) {
        TODO("Not yet implemented")
    }

    override fun allHomeWeatherData(): Flow<Root> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRootData() {
        TODO("Not yet implemented")
    }

    override suspend fun insertRoot(root: Root) {
        TODO("Not yet implemented")
    }

    override suspend fun getRoot(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
       return api.getRoot(
            latitude,
            longitude,
            appid,
            units,
            lang
        )
    }


}