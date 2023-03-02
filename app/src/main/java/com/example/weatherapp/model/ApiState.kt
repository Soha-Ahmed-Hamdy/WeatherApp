package com.example.weatherapp.model

import androidx.localbroadcastmanager.R

sealed class ApiState{
    class Success(val weatherRoot: Root?):ApiState()
    class Failure(val msg: Throwable):ApiState()
    object Loading:ApiState()
}
