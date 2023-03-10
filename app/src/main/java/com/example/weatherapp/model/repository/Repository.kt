package com.example.weatherapp.model.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.model.*
import com.example.weatherapp.network.Api
import com.example.weatherapp.network.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Repository (var context:Context){

    val apiObj = RetrofitHelper.retrofit.create(Api::class.java)

    fun getStates(){
        SharedPrefData.setupSharedPrefrences(context)
    }

    private suspend fun getRoot(): Flow<Root> = flow{
        getStates()
            emit(apiObj.getRoot(
                SharedPrefData.latitude.toDouble()
                , SharedPrefData.longitude.toDouble()
                //,"bec88e8dd2446515300a492c3862a10e"
                //,"d9abb2c1d05c5882e937cffd1ecd4923"
                //,"44c59959fbe6086cb77fb203967bbc0c"
                //,"f112a761188e9c22cdf3eb3a44597b00"
                ,"489da633b031b5fa008c48ee2deaf025"
                ,SharedPrefData.unit
                ,SharedPrefData.language).body()!!
            )
    }

    suspend fun getFavDetails(lat: Double,long: Double): Flow<Root?> {
        return flow {
            val result= apiObj.getRoot(lat,long).body()!!
            emit(result)
        }

    }

    suspend fun getHomeData(): Flow<Root>? {

            if(checkForInternet()) {
               return getRoot().also {
                    deleteHomeData()
                    it.collect{
                        insertRoot(it)

                    }
                }
            }else{
              return  getLocalRoot()
            }

    }

    suspend fun insertFavCountry(favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.insertFavourite(favPlace)
    }
    suspend fun deleteFavCountry(favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.deleteFavouritePlace(favPlace)
    }

    fun getAllFavCountry()=
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.allFavouriteWeather()

    fun getLocalRoot() =
        AppDatabase.getInstance(context)?.homeDAO()?.allHomeWeatherData()

    suspend fun deleteHomeData()=
        AppDatabase.getInstance(context)?.homeDAO()?.deleteRootData()
    suspend fun insertRoot(root: Root)=
        AppDatabase.getInstance(context)?.homeDAO()?.insertRoot(root)

    suspend fun insertAlert(alert: LocalAlert){
        AppDatabase.getInstance(context)?.alertDAO()?.insertAlert(alert)
    }
    suspend fun deleteAlert(alert: LocalAlert){
        AppDatabase.getInstance(context)?.alertDAO()?.deleteAlert(alert)
    }

    fun getAllAlerts()=
        AppDatabase.getInstance(context)?.alertDAO()?.allAlerts()


    fun checkForInternet(): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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