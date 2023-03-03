package com.example.weatherapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.repository.Repository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utility {
companion object{

    val Language_EN_Value : String = "en"
    val Language_AR_Value : String = "ar"
    val Language_Key : String = "Lang"
    val TEMP_KEY : String = "Temp"
    val IMPERIAL : String = "imperial"
    val STANDARD : String = "standard"
    val METRIC : String = "metric"
    val Language_Value_Key: String ="Language"
    val LOCATION_KEY : String = "Location"
    val MAP : String = "map"
    val GPS : String = "gps"
    val LONGITUDE_KEY : String = "Longitude"
    val LATITUDE_KEY : String = "Latitude"
    val LAT : String = "0.0"
    val LONG : String = "0.0"

    fun checkUnit():String{
            var tempMeasure:String?=null
            if(Repository.language== Language_EN_Value){
                if(Repository.unit== IMPERIAL){
                    tempMeasure=" °F"
                }else if(Repository.unit== STANDARD){
                    tempMeasure=" °K"
                }else{
                    tempMeasure=" °C"
                }
            }else{
                if(Repository.unit== IMPERIAL){
                    tempMeasure=" °ف"
                }else if(Repository.unit== STANDARD){
                    tempMeasure=" °كلفن"
                }else{
                    tempMeasure=" °م"
                }

            }
            return tempMeasure
        }

    fun saveLanguageToSharedPref(context: Context, key: String, value: String){
        var editor : SharedPreferences.Editor = context.getSharedPreferences("Language",
        AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun timeNameOfToDay(dt: Long) : String{
        var date: Date = Date(dt * 1000)
        var dateFormat : DateFormat = SimpleDateFormat("EEEE")
        return dateFormat.format(date)
    }

    fun saveTempToSharedPref(context: Context, key : String, value : String){
        var editor : SharedPreferences.Editor = context.getSharedPreferences("Units",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLatitudeToSharedPref(context: Context, key : String, value : Long){
        var editor : SharedPreferences.Editor = context.getSharedPreferences("Latitude",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveLongitudeToSharedPref(context: Context, key : String, value : Long){
        var editor : SharedPreferences.Editor = context.getSharedPreferences("Longitude",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveLocationToSharedPref(context: Context, key : String, value : String){
        var editor : SharedPreferences.Editor = context.getSharedPreferences("Location",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun timeStampToHour(dt : Long) : String{
        var date: Date = Date(dt * 1000)
        var dateFormat : DateFormat = SimpleDateFormat("h:mm a")
        return dateFormat.format(date)
    }
    fun getWeatherStatusIcon(imageString: String): Int {
        val imageInInteger: Int
        when (imageString) {
            "01d" -> imageInInteger = R.drawable.icon_01d
            "01n" -> imageInInteger = R.drawable.icon_01n
            "02d" -> imageInInteger = R.drawable.icon_02d
            "02n" -> imageInInteger = R.drawable.icon_02n
            "03n" -> imageInInteger = R.drawable.icon_03n
            "03d" -> imageInInteger = R.drawable.icon_03d
            "04d" -> imageInInteger = R.drawable.icon_04d
            "04n" -> imageInInteger = R.drawable.icon_04n
            "09d" -> imageInInteger = R.drawable.icon_09d
            "09n" -> imageInInteger = R.drawable.icon_09n
            "10d" -> imageInInteger = R.drawable.icon_10d
            "10n" -> imageInInteger = R.drawable.icon_10n
            "11d" -> imageInInteger = R.drawable.icon_11d
            "11n" -> imageInInteger = R.drawable.icon_11n
            "13d" -> imageInInteger = R.drawable.icon_13d
            "13n" -> imageInInteger = R.drawable.icon_13n
            "50d" -> imageInInteger = R.drawable.icon_50d
            "50n" -> imageInInteger = R.drawable.icon_50n
            else -> imageInInteger = R.drawable.cloud
        }
        return imageInInteger
    }

    fun convertNumbersToArabic(value: Int): String {    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")}
    fun convertNumbersToArabic(value: Long): String {    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")}
    fun convertNumbersToArabic(value: Double): String {    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")}
    fun timeStampToDate (dt : Long) : String{
        var date : Date = Date(dt * 1000)
        var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
        return dateFormat.format(date)
    }
}


}