package com.example.weatherapp.ui.alert.alertViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.repository.Repository

class FactoryAlert(var repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repository) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}