package com.example.weatherapp.ui.favourite.favouriteViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteViewModel(context: Context) : ViewModel() {

    var repo = Repository

    private var _favWeather=MutableLiveData<List<FavouritePlace>>()
    val favWeather:LiveData<List<FavouritePlace>> = _favWeather



    init {
        getFav(context)

    }
    fun getFav(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            _favWeather.postValue(repo.getAllFavCountry(context))
        }
    }

    fun insertFav(context: Context, fav: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertFavCountry(context,fav)
            getFav(context)
        }
    }

    fun deleteFav(context: Context, fav: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteFavCountry(context,fav)
            getFav(context)
        }
    }

    fun checkConnectivity(context: Context):Boolean{
        return repo.checkForInternet(context)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}