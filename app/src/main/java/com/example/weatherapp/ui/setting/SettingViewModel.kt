package com.example.weatherapp.ui.setting

import androidx.lifecycle.ViewModel
import android.content.Context
import com.example.weatherapp.repository.Repository

class SettingViewModel (context : Context) :ViewModel() {
    var repo = Repository


    init {
        getStates(context)
    }


    fun getStates(context: Context){
        repo.setupSharedPrefrences(context)
    }
}