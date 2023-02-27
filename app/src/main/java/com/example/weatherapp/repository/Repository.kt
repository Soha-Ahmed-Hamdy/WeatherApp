package com.example.weatherapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.favouritePlaceDAO
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root
import com.example.weatherapp.network.Api
import com.example.weatherapp.network.RetrofitHelper
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

object Repository {

    val apiObj = RetrofitHelper.retrofit.create(Api::class.java)

    suspend fun getRoot(lat: Double
                        ,long: Double
    ): Root = apiObj.getRoot(lat
        ,long
        ,"bec88e8dd2446515300a492c3862a10e"
        ,"metric"
        ,"en")?.body()!!

    suspend fun getFavDetails(context: Context,lat: Double,long: Double): Root?{
        var result: Root?
        if(checkForInternet(context)){
            result=getRoot(lat,long)

        }else{
            result=null
        }
        return result
    }


    suspend fun getHomeData(context: Context,lat: Double,long: Double): Root?{
        var result: Root?
        if(checkForInternet(context)){
            result=getRoot(lat,long)
            deleteHomeData(context)
            insertRoot(context,getRoot(lat,long)!!)
        }else{
            result=getLocalRoot(context)
        }
        return result
    }



//    suspend fun getDaily(): List<Daily> = apiObj.getRoot(30.6210748
//        ,32.2687504
//        ,"bec88e8dd2446515300a492c3862a10e"
//        ,"metric"
//        ,"en")?.body()!!.daily
//
//    suspend fun getHourly(): List<Current> = apiObj.getRoot(30.6210748
//        ,32.2687504
//        ,"bec88e8dd2446515300a492c3862a10e"
//        ,"metric"
//        ,"en")?.body()!!.hourly
//
//    suspend fun getCurrentDayWeather(): Current? = apiObj.getRoot(30.6210748
//        ,32.2687504
//        ,"bec88e8dd2446515300a492c3862a10e"
//        ,"metric"
//        ,"en")?.body()!!.current

    suspend fun insertFavCountry(context: Context,favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.insertFavourite(favPlace)
    }
    suspend fun deleteFavCountry(context: Context,favPlace: FavouritePlace){
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.deleteFavouritePlace(favPlace)
    }

    suspend fun getAllFavCountry(context: Context): List<FavouritePlace>? =
        AppDatabase.getInstance(context)?.favouritePlaceDAO()?.allFavouriteWeather()

    suspend fun getLocalRoot(context: Context): Root? =
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