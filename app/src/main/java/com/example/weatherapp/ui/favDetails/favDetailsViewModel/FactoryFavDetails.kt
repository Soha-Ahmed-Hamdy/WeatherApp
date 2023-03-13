package com.example.weatherapp.ui.favDetails.favDetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface

class FactoryFavDetails(var repository : RepositoryInterface, private val lat:Double, private val long:Double): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteDetailsViewModel::class.java)){
            FavouriteDetailsViewModel(repository,lat,long) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}