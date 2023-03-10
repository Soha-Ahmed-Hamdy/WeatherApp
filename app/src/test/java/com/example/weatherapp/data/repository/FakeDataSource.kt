package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    private var favList:MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(),
    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(),
    private var rootList: MutableList<Root> = mutableListOf<Root>()

) : DataSource{



    override fun allAlerts(): Flow<List<LocalAlert>> = flow {
        emit(alertList)
    }

    override suspend fun insertAlert(alert: LocalAlert) {
        alertList.add(alert)
    }

    override suspend fun deleteAlert(alert: LocalAlert) {
        alertList.remove(alert)
    }

    override fun allFavouriteWeather(): Flow<List<FavouritePlace>> = flow {
        emit(favList)
    }

    override suspend fun insertFavourite(favPlace: FavouritePlace?) {
        favList.add(favPlace!!)
    }

    override suspend fun deleteFavouritePlace(favPlace: FavouritePlace?) {
        favList.remove(favPlace)
    }

    override fun allHomeWeatherData(): Flow<Root> = flow {
            emit(rootList[0])
    }

    override suspend fun deleteRootData() {
        rootList.clear()
    }

    override suspend fun insertRoot(root: Root) {
        rootList.add(root)
    }

    override suspend fun getRoot(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return Response.success(Root())
    }

}