package com.example.weatherapp.ui.setting

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.Repository

class SettingViewModel (var repository : Repository) :ViewModel() {

    init {
        getStates()
    }

    fun getStates(){
        repository.getStates()
    }
    fun checkConnectivity():Boolean{
        return repository.checkForInternet()
    }
}