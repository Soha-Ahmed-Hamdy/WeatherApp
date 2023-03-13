package com.example.weatherapp.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.data.database.AppDatabase
import com.example.weatherapp.data.*
import com.example.weatherapp.data.database.LocalDataSource
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.network.Api
import com.example.weatherapp.data.network.RemoteDataSource
import com.example.weatherapp.data.network.RetrofitHelper
import com.example.weatherapp.data.utils.SharedPrefData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Repository(
    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource,
    private val context: Context
) : RepositoryInterface {
    companion object{
        @Volatile
        private var INSTANCE: Repository?=null

        fun getRepositoryInstance(app: Application): Repository{
            return INSTANCE?: synchronized(this){
                val database = AppDatabase.getInstance(app)
                val api = RetrofitHelper.retrofit.create(Api::class.java)
                val localSource = LocalDataSource(
                    database.alertDAO(),
                    database.favouritePlaceDAO(),
                    database.homeDAO()
                )
                val remoteDataSource =RemoteDataSource(api)
                Repository(
                    remoteDataSource,
                    localSource,
                    app.applicationContext
                )
            }
        }

    }

    override fun getStates() {
        SharedPrefData.setupSharedPrefrences(context)
    }

    private suspend fun getRoot(): Flow<Root> = flow {
        getStates()
        emit(
            remoteDataSource.getRoot(
                SharedPrefData.latitude.toDouble()
                , SharedPrefData.longitude.toDouble()
                //,"bec88e8dd2446515300a492c3862a10e"
                ,"d9abb2c1d05c5882e937cffd1ecd4923"
                //,"44c59959fbe6086cb77fb203967bbc0c"
                //,"f112a761188e9c22cdf3eb3a44597b00"
                //, "489da633b031b5fa008c48ee2deaf025", SharedPrefData.unit, SharedPrefData.language
            ).body()!!
        )
    }

    override suspend fun getFavDetails(lat: Double, long: Double): Flow<Root?> {
        return flow {
            val result = remoteDataSource.getRoot(lat, long).body()!!
            emit(result)
        }

    }

    override suspend fun getHomeData(): Flow<Root>? {

        if (checkForInternet()) {
            return getRoot().also {
                deleteHomeData()
                it.collect {
                    insertRoot(it)

                }
            }
        } else {
            return getLocalRoot()
        }

    }

    override suspend fun insertFavCountry(favPlace: FavouritePlace) {
        localDataSource.insertFavourite(favPlace)
    }

    override suspend fun deleteFavCountry(favPlace: FavouritePlace) {
        localDataSource.deleteFavouritePlace(favPlace)
    }

    override fun getAllFavCountry() =
        localDataSource.allFavouriteWeather()

    private fun getLocalRoot() =
        localDataSource.allHomeWeatherData()

    private suspend fun deleteHomeData() =
        localDataSource.deleteRootData()

    private suspend fun insertRoot(root: Root) =
        localDataSource.insertRoot(root)

    override suspend fun insertAlert(alert: LocalAlert) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: LocalAlert) {
        localDataSource.deleteAlert(alert)
    }

    override fun getAllAlerts() =
        localDataSource.allAlerts()

    override fun checkForInternet(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}