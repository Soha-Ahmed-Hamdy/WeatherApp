package com.example.weatherapp.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FactorySetting(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(context) as T
        }else{
            throw java.lang.IllegalArgumentException("error")
        }
    }
}