package com.example.weatherapp.ui.home.HomeViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.*
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(context: Context,lat: Double,long: Double) : ViewModel() {

    var repo = Repository


    private var _rootWeather= MutableStateFlow<ApiState>(ApiState.Loading)
    val rootWeather= _rootWeather.asStateFlow()


    init {
        getRootHome(context, lat, long)
    }

    fun getRootHome(context: Context,lat: Double,long: Double)=
        viewModelScope.launch {

            repo.getHomeData(context, lat, long)
                ?.catch { e ->
                _rootWeather.value = ApiState.Failure(e)
            }
                ?.collect {data->
                _rootWeather.value = ApiState.Success(data)
            }
        }

}