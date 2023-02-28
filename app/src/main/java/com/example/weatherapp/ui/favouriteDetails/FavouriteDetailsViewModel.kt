package com.example.weatherapp.ui.favouriteDetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.*
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(
                         context: Context
                         ,lat: Double
                         ,long: Double) : ViewModel() {

    var repo = Repository

    private var _favDetailsWeather=MutableLiveData<Root>()
    val favDetailsWeather:LiveData<Root> = _favDetailsWeather

    private var _stateFlow= MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow = _stateFlow.asStateFlow()


    init {
        getFavRootDetails(context,lat,long)
    }
    fun getFavRootDetails(context: Context, lat: Double, long: Double){
        viewModelScope.launch{
            repo.getFavDetails(context,lat,long)
                .catch {
                    _stateFlow.value=ApiState.Failure(it)
                }
                .collect{
                    _stateFlow.value= ApiState.Success(it)

                }
        }
//        viewModelScope.launch (Dispatchers.IO){
//            _favDetailsWeather.postValue(repo.getFavDetails(context,lat,long))
//        }
    }

}