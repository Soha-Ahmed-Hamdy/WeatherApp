package com.example.weatherapp.data.utils

import com.example.weatherapp.data.model.FavouritePlace

sealed class RoomState{

    class Success(val countrysFav: List<FavouritePlace>): RoomState()
    class Failure(val msg: Throwable): RoomState()
    object Loading: RoomState()

}
