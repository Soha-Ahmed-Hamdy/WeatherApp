package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.utils.SharedPrefData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("onecall")
    suspend fun getRoot(
        @Query("lat") latitude: Double
        , @Query("lon") longitude: Double
        , @Query("appid") appid: String="bec88e8dd2446515300a492c3862a10e"
        //, @Query("appid") appid: String="4a059725f93489b95183bbcb8c6829b9"
        //, @Query("appid") appid: String="d9abb2c1d05c5882e937cffd1ecd4923"
        //, @Query("appid") appid: String="f112a761188e9c22cdf3eb3a44597b00"
        //, @Query("appid") appid: String="489da633b031b5fa008c48ee2deaf025"
        , @Query("units") units: String= SharedPrefData.unit
        , @Query("lang") lang: String= SharedPrefData.language
    ): Response<Root>

}
object RetrofitHelper{
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

}
