package com.example.weatherapp.ui.favourite.favouriteViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.RoomState
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavouriteViewModel(context: Context) : ViewModel() {

    var repo = Repository

    private var _favWeather= MutableStateFlow<RoomState>(RoomState.Loading)
    val favWeather = _favWeather.asStateFlow()



    init {
        getFav(context)

    }
    fun getFav(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            repo.getAllFavCountry(context)
                ?.catch {
                    _favWeather.value=RoomState.Failure(it)
                }?.collect{
                    _favWeather.value=RoomState.Success(it)
                }
        }
    }

    fun insertFav(context: Context, fav: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertFavCountry(context,fav)
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