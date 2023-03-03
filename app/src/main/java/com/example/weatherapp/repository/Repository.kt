package com.example.weatherapp.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.LocalAlert
import com.example.weatherapp.model.Root
import com.example.weatherapp.network.Api
import com.example.weatherapp.network.RetrofitHelper
import com.example.weatherapp.ui.Utility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object Repository {

    val apiObj = RetrofitHelper.retrofit.create(Api::class.java)

    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared : SharedPreferences
    lateinit var notificationShared : SharedPreferences
    lateinit var locationShared : SharedPreferences
    lateinit var longitudeShared : SharedPreferences
    lateinit var latitudeShared : SharedPreferences

     var language:String="en"
    lateinit var unit:String
    lateinit var location:String
    var longitude:Long = 30.6210725.toLong()
    var latitude:Long = 32.2687095.toLong()


    fun setupSharedPrefrences(context : Context){
        languageSharedPreferences = context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared  = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        notificationShared  = context.getSharedPreferences("Notification", AppCompatActivity.MODE_PRIVATE)
        locationShared = context.getSharedPreferences("Location",AppCompatActivity.MODE_PRIVATE)
        longitudeShared = context.getSharedPreferences("Longitude",AppCompatActivity.MODE_PRIVATE)
        latitudeShared = context.getSharedPreferences("Latitude",AppCompatActivity.MODE_PRIVATE)

        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY,"metric")!!
        location = locationShared.getString(Utility.LOCATION_KEY,"gps")!!
        longitude = longitudeShared.getLong(Utility.LONGITUDE_KEY,30.6210725.toLong())!!
        latitude = latitudeShared.getLong(Utility.LATITUDE_KEY,32.2687095.toLong())!!


    }

    private suspend fun getRoot(context: Context,lat: Double
                                , long: Double
    ): Flow<Root> = flow{
        setupSharedPrefrences(context)
        //if(location==Utility.MAP){
            emit(apiObj.getRoot(
                latitude.toDouble()
                , longitude.toDouble()
                //,"bec88e8dd2446515300a492c3862a10e"
                ,"d9abb2c1d05c5882e937cffd1ecd4923"
                //,"44c59959fbe6086cb77fb203967bbc0c"
                //,"f112a761188e9c22cdf3eb3a44597b00"
                //,"489da633b031b5fa008c48ee2deaf025"
                ,unit
                ,language).body()!!
            )
        /*}
        else{
            emit(apiObj.getRoot(
                lat
                , long
                //,"bec88e8dd2446515300a492c3862a10e"
                ,"d9abb2c1d05c5882e937cffd1ecd4923"
                //,"44c59959fbe6086cb77fb203967bbc0c"
                //,"f112a761188e9c22cdf3eb3a44597b00"
                ,unit
                , language).body()!!
            )

        }*/

    }

    suspend fun getFavDetails(context: Context,lat: Double,long: Double): Flow<Root?> {
        return flow {
            val result= apiObj.getRoot(lat,long).body()!!
            emit(result)
        }

    }

    suspend fun getHomeData(context: Context,lat: Double,long: Double): Flow<Root>? {

            if(checkForInternet(context)) {
               return getRoot(context ,lat, long).also {
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

    suspend fun insertAlert(context: Context,alert: LocalAlert){
        AppDatabase.getInstance(context)?.alertDAO()?.insertAlert(alert)
    }
    suspend fun deleteAlert(context: Context,alert: LocalAlert){
        AppDatabase.getInstance(context)?.alertDAO()?.deleteAlert(alert)
    }

    fun getAllAlerts(context: Context)=
        AppDatabase.getInstance(context)?.alertDAO()?.allAlerts()


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