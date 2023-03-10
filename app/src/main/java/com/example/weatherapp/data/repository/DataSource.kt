package com.example.weatherapp.data.repository


import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.utils.SharedPrefData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {

    fun allAlerts(): Flow<List<LocalAlert>>

    suspend fun insertAlert(alert: LocalAlert)

    suspend fun deleteAlert(alert: LocalAlert)

    fun allFavouriteWeather(): Flow<List<FavouritePlace>>

    suspend fun insertFavourite(favPlace: FavouritePlace?)

    suspend fun deleteFavouritePlace(favPlace: FavouritePlace?)

    fun allHomeWeatherData(): Flow<Root>

    suspend fun deleteRootData()

    suspend fun insertRoot(root: Root)

    suspend fun getRoot(
        latitude: Double
        , longitude: Double
        //, appid: String="bec88e8dd2446515300a492c3862a10e"
        //, appid: String="d9abb2c1d05c5882e937cffd1ecd4923"
        //, appid: String="f112a761188e9c22cdf3eb3a44597b00"
        ,appid: String="489da633b031b5fa008c48ee2deaf025"
        , units: String= SharedPrefData.unit
        , lang: String= SharedPrefData.language
    ): Response<Root>
}