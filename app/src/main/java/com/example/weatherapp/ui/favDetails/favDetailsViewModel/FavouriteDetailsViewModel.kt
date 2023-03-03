package com.example.weatherapp.ui.favDetails.favDetailsViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(context: Context, lat: Double, long: Double) : ViewModel() {

    var repo = Repository


    private var _stateFlow= MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow= _stateFlow.asStateFlow()


    init {
        getFavRootDetails(context, lat, long)
    }

    fun getFavRootDetails(context: Context, lat: Double, long: Double)=
        viewModelScope.launch {

            repo.getFavDetails(context, lat, long)
                ?.catch { e ->
                    _stateFlow.value = ApiState.Failure(e)
                }
                ?.collect {data->
                    _stateFlow.value = ApiState.Success(data)
                }
        }

}