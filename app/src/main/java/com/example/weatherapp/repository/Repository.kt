package com.example.weatherapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root
import com.example.weatherapp.network.Api
import com.example.weatherapp.network.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


object Repository {

    val apiObj = RetrofitHelper.retrofit.create(Api::class.java)

    private suspend fun getRoot(lat: Double
                                , long: Double
    ): Flow<Root> = flow{
        emit(apiObj.getRoot(lat
            ,long
            ,"bec88e8dd2446515300a492c3862a10e"
            ,"metric"
            ,"en").body()!!
        )
    }

    suspend fun getFavDetails(context: Context,lat: Double,long: Double): Flow<Root?> {
        return flow {
            val result= apiObj.getRoot(lat,long).body()!!
            emit(result)
        }

    }

    suspend fun getHomeData(context: Context,lat: Double,long: Double): Flow<Root>? {

            if(checkForInternet(context)) {
               return getRoot(lat, long).also {
                    deleteHomeData(context)
                    it.collect{
                        insertRoot(context,it)

                    }
                }
            }else{
              return  getLocalRoot(context)
            }

    }

    suspend fun insertFavCountry(context: Context,favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.insertFavourite(favPlace)
    }
    suspend fun deleteFavCountry(context: Context,favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.deleteFavouritePlace(favPlace)
    }

    fun getAllFavCountry(context: Context)=
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.allFavouriteWeather()

    fun getLocalRoot(context: Context) =
        AppDatabase.getInstance(context)?.homeDAO()?.allHomeWeatherData()

    suspend fun deleteHomeData(context: Context)=
        AppDatabase.getInstance(context)?.homeDAO()?.deleteRootData()
    suspend fun insertRoot(context: Context, root: Root)=
        AppDatabase.getInstance(context)?.homeDAO()?.insertRoot(root)
    fun checkForInternet(context: Context): Boolean {

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