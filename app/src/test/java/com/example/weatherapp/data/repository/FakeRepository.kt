package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

class FakeRepository(

    var favouriteList:MutableList<FavouritePlace> = mutableListOf(),
    var alertList: MutableList<LocalAlert> = mutableListOf(),
    var rootElement: Root = Root()
): RepositoryInterface{


    override fun getStates() {
        TODO("Not yet implemented")
    }


    override suspend fun getFavDetails(lat: Double, long: Double): Flow<Root?> = flow{
        emit(rootElement)
    }

    override suspend fun getHomeData(): Flow<Root>? = flow {
        emit(rootElement)
    }

    override suspend fun insertFavCountry(favPlace: FavouritePlace) {
        favouriteList.add(favPlace)
    }

    override suspend fun deleteFavCountry(favPlace: FavouritePlace) {
        favouriteList.remove(favPlace)
    }

    override fun getAllFavCountry(): Flow<List<FavouritePlace>> = flow {
        emit(favouriteList)

    }

    override suspend fun insertAlert(alert: LocalAlert) {
        alertList.add(alert)
    }

    override suspend fun deleteAlert(alert: LocalAlert) {
        alertList.remove(alert)
    }

    override fun getAllAlerts(): Flow<List<LocalAlert>> =flow {
        emit(alertList)
    }

    override fun checkForInternet(): Boolean {
        TODO("Not yet implemented")
    }


}