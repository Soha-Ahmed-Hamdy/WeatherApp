package com.example.weatherapp.data.utils

import com.example.weatherapp.data.model.Root

sealed class ApiState{
    class Success(val weatherRoot: Root?): ApiState()
    class Failure(val msg: Throwable): ApiState()
    object Loading: ApiState()
}
