package com.example.weatherapp.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {
companion object{

    const val Language_EN_Value : String = "en"
    const val Language_AR_Value : String = "ar"
    const val Language_Key : String = "Lang"
    const val TEMP_KEY : String = "Temp"
    const val IMPERIAL : String = "imperial"
    const val STANDARD : String = "standard"
    const val METRIC : String = "metric"
    const val Language_Value_Key: String ="Language"
    const val LOCATION_KEY : String = "Location"
    const val MAP : String = "map"
    const val GPS : String = "gps"
    const val LONGITUDE_KEY : String = "Longitude"
    const val LATITUDE_KEY : String = "Latitude"


    fun checkUnit():String{
            val tempMeasure:String
            if(SharedPrefData.language == Language_EN_Value){
                tempMeasure = when (SharedPrefData.unit) {
                    IMPERIAL -> {
                        " °F"
                    }
                    STANDARD -> {
                        " °K"
                    }
                    else -> {
                        " °C"
                    }
                }
            }else{
                tempMeasure = when (SharedPrefData.unit) {
                    IMPERIAL -> {
                        " °ف"
                    }
                    STANDARD -> {
                        " °كلفن"
                    }
                    else -> {
                        " °م"
                    }
                }

            }
            return tempMeasure
        }

    fun saveLanguageToSharedPref(context: Context, key: String, value: String){
        val editor : SharedPreferences.Editor = context.getSharedPreferences("Language",
        AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun timeNameOfToDay(dt: Long) : String{
        val date= Date(dt * 1000)
        val dateFormat : DateFormat = SimpleDateFormat("EEEE")
        return dateFormat.format(date)
    }

    fun saveTempToSharedPref(context: Context, key : String, value : String){
        val editor : SharedPreferences.Editor = context.getSharedPreferences("Units",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLatitudeToSharedPref(context: Context, key : String, value : Long){
        val editor : SharedPreferences.Editor = context.getSharedPreferences("Latitude",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveLongitudeToSharedPref(context: Context, key : String, value : Long){
        val editor : SharedPreferences.Editor = context.getSharedPreferences("Longitude",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveLocationToSharedPref(context: Context, key : String, value : String){
        val editor : SharedPreferences.Editor = context.getSharedPreferences("Location",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun timeStampToHour(dt : Long) : String{
        val date= Date(dt * 1000)
        val dateFormat : DateFormat = SimpleDateFormat("h:mm a")
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
        val date = Date(dt * 1000)
        val dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
        return dateFormat.format(date)
    }
    fun dateToLong(date: String?): Long {
        val f = SimpleDateFormat("dd-MM-yyyy")
        var milliseconds: Long = 0
        try {
            val d = date?.let { f.parse(it) }
            milliseconds = d!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return milliseconds
    }

    fun longToDate (dt : Long) : String{
        val date = Date(dt)
        val dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
        return dateFormat.format(date)
    }

    fun timeToLong (str : String) : Long{
        val date = str.split(":")
        val minutesSum = (date[0].toInt()*60)+date[1].toInt()
        return minutesSum.toLong()
    }

    fun LongToTime (time : Long) : String{
        var hour = time.toInt()/60
        var min = time.toInt()%60
        var am_pm = ""

        when {
            hour == 0 -> {
                hour += 12
                am_pm = "AM"
            }
            hour == 12 -> am_pm = "PM"
            hour > 12 -> {
                hour -= 12
                am_pm = "PM"
            }
            else -> am_pm = "AM"
        }
        val date = "$hour:$min $am_pm"
        return date
    }
}
}