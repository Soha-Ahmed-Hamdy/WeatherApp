package com.example.weatherapp.ui.favourite.favouriteViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface

class FactoryFavouriteWeather(var repository: RepositoryInterface):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(repository) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}