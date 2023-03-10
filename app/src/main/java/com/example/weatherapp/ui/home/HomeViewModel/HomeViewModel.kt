package com.example.weatherapp.ui.home.HomeViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.ApiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(var repository :Repository) : ViewModel() {

    private var _rootWeather= MutableStateFlow<ApiState>(ApiState.Loading)
    val rootWeather= _rootWeather.asStateFlow()


    init {
        getRootHome()
        getStates()
    }
    fun getStates(){
        repository.getStates()
    }

    fun getRootHome()=
        viewModelScope.launch {

            repository.getHomeData()
                ?.catch { e ->
                _rootWeather.value = ApiState.Failure(e)
            }
                ?.collect {data->
                _rootWeather.value = ApiState.Success(data)
            }
        }

}