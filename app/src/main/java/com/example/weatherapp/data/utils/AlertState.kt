package com.example.weatherapp.data.utils

import com.example.weatherapp.data.model.LocalAlert

sealed class AlertState{
    class Success(val alertsData: List<LocalAlert>): AlertState()
    class Failure(val msg: Throwable): AlertState()
    object Loading: AlertState()
}
