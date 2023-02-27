package com.example.weatherapp.ui.home.HomeViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class FactoryHomeWeather(private val context: Context,private val lat:Double,private val long:Double): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(context,lat,long) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}