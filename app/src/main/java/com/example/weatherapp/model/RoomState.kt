package com.example.weatherapp.model

sealed class RoomState{

    class Success(val countrysFav: List<FavouritePlace>):RoomState()
    class Failure(val msg: Throwable):RoomState()
    object Loading:RoomState()

}
