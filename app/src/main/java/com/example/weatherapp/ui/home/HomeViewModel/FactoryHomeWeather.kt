package com.example.weatherapp.ui.home.HomeViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.repository.Repository
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class FactoryHomeWeather(var repository : Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repository) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}