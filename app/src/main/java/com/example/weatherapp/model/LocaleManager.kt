package com.example.weatherapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LocaleManager {

    companion object{
        fun setLocale(context: Context){
            setNewLocale(context, getLanguage(context))
            update(context, getLanguage(context))
        }

        private fun setNewLocale(context: Context, language : String){
            persistLanguage(context, language)
            update(context, language)
        }

        private fun update(context: Context, language: String){
            updateResources(context, language)
            val appContext : Context = context.applicationContext
            if(context != appContext){
                updateResources(appContext, language)
            }
        }

        private fun getLanguage(context: Context) : String{
            val languageShared : SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
            return languageShared.getString(Utility.Language_Key, "en") ?: ""
        }

        private fun persistLanguage(context: Context, language: String){
            Utility.saveLanguageToSharedPref(context, Utility.Language_Key, language)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun updateResources(context: Context, language: String){
            val locale = Locale(language)
            Locale.setDefault(locale)

            val resources : Resources = context.resources
            val config = Configuration(resources.configuration)
            if(Build.VERSION.SDK_INT >= 17){
                config.setLocale(locale)
                //context = context.createConfigurationContext(config)
            }else{
                config.locale = locale
            }

            resources.updateConfiguration(config, resources.displayMetrics)
        }

    }
}