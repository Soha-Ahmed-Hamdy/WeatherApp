package com.example.weatherapp.ui.favouriteDetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel

class FactoryFavDetails(private val context: Context, private val lat:Double, private val long:Double): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteDetailsViewModel::class.java)){
            FavouriteDetailsViewModel(context,lat,long) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}