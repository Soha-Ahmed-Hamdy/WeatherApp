package com.example.weatherapp.ui.alert.alertViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class FactoryAlert(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(context) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}