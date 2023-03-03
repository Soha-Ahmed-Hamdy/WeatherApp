package com.example.weatherapp.model

sealed class AlertState{
    class Success(val alertsData: List<LocalAlert>):AlertState()
    class Failure(val msg: Throwable):AlertState()
    object Loading:AlertState()
}
