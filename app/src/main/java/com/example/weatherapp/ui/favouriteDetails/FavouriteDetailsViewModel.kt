package com.example.weatherapp.ui.favouriteDetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel(
                         context: Context
                         ,lat: Double
                         ,long: Double) : ViewModel() {

    var repo = Repository

    private var _favDetailsWeather=MutableLiveData<Root>()
    val favDetailsWeather:LiveData<Root> = _favDetailsWeather


    init {
        getFavRootDetails(context,lat,long)
    }
    fun getFavRootDetails(context: Context, lat: Double, long: Double){
        viewModelScope.launch (Dispatchers.IO){
            _favDetailsWeather.postValue(repo.getFavDetails(context,lat,long))
        }
    }

}