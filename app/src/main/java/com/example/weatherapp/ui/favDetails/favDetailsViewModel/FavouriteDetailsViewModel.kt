package com.example.weatherapp.ui.favDetails.favDetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.utils.ApiState
import com.example.weatherapp.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(var repository : Repository,lat: Double, long: Double) : ViewModel() {

    private var _stateFlow= MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow= _stateFlow.asStateFlow()


    init {
        getFavRootDetails(lat, long)
    }

    fun getFavRootDetails(lat: Double, long: Double)=
        viewModelScope.launch {

            repository.getFavDetails(lat, long)
                .catch { e ->
                    _stateFlow.value = ApiState.Failure(e)
                }
                .collect {data->
                    _stateFlow.value = ApiState.Success(data)
                }
        }

}