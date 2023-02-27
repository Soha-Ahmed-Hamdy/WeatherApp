package com.example.weatherapp.ui.favourite.favouriteViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FactoryFavouriteWeather(private val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(context) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}