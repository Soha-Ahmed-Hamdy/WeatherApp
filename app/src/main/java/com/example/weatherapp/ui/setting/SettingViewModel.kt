package com.example.weatherapp.ui.setting

import androidx.lifecycle.ViewModel
import android.content.Context
import com.example.weatherapp.model.repository.Repository

class SettingViewModel (var repository : Repository) :ViewModel() {

    init {
        getStates()
    }

    fun getStates(){
        repository.getStates()
    }
}