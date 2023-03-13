package com.example.weatherapp.data.utils

import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object SharedPrefData {
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared : SharedPreferences
    lateinit var notificationShared : SharedPreferences
    lateinit var locationShared : SharedPreferences
    lateinit var longitudeShared : SharedPreferences
    lateinit var latitudeShared : SharedPreferences
    lateinit var themeShared : SharedPreferences
    lateinit var placeShared : SharedPreferences


    var language:String="en"
    lateinit var unit:String
    lateinit var location:String
    lateinit var notification: String
    lateinit var theme :String
    var longitude:Long = 30.6210725.toLong()
    var latitude:Long = 32.2687095.toLong()
    lateinit var place :String

    fun setupSharedPrefrences(context:Context){
        languageSharedPreferences = context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared  = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        notificationShared  = context.getSharedPreferences("Notification", AppCompatActivity.MODE_PRIVATE)
        locationShared = context.getSharedPreferences("Location", AppCompatActivity.MODE_PRIVATE)
        longitudeShared = context.getSharedPreferences("Longitude", AppCompatActivity.MODE_PRIVATE)
        latitudeShared = context.getSharedPreferences("Latitude", AppCompatActivity.MODE_PRIVATE)
        themeShared = context.getSharedPreferences("Theme",AppCompatActivity.MODE_PRIVATE)
        placeShared = context.getSharedPreferences("Place",AppCompatActivity.MODE_PRIVATE)


        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY,"metric")!!
        notification = notificationShared.getString(Utility.NOTIFICATION_KEY,"notify")!!
        location = locationShared.getString(Utility.LOCATION_KEY,"gps")!!
        longitude = longitudeShared.getLong(Utility.LONGITUDE_KEY,30.6210725.toLong())
        latitude = latitudeShared.getLong(Utility.LATITUDE_KEY,32.2687095.toLong())
        theme = themeShared.getString(Utility.Theme_KEY,"light")!!
        place = placeShared.getString(Utility.PLACE_KEY,"الشيخ زايد")!!


    }
}