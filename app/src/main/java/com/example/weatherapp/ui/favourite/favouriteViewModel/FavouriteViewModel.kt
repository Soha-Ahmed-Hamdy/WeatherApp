package com.example.weatherapp.ui.favourite.favouriteViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.utils.RoomState
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavouriteViewModel(var repository : RepositoryInterface) : ViewModel() {

    private var _favWeather= MutableStateFlow<RoomState>(RoomState.Loading)
    val favWeather = _favWeather.asStateFlow()

    init {
        getFav()

    }
    fun getFav(){
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllFavCountry()
                ?.catch {
                    _favWeather.value= RoomState.Failure(it)
                }?.collect{
                    _favWeather.value= RoomState.Success(it)
                }
        }
    }

    fun insertFav(fav: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertFavCountry(fav)
        }
    }

    fun deleteFav(fav: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteFavCountry(fav)
            getFav()
        }
    }

    fun checkConnectivity():Boolean{
        return repository.checkForInternet()
    }

}