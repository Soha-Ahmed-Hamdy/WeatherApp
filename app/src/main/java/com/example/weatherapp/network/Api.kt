package com.example.weatherapp.network

import com.example.weatherapp.model.Root
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
        , @Query("units") units: String="metric"
        , @Query("lang") lang: String="en"
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
