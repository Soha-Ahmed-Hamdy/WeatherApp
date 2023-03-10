package com.example.weatherapp.ui.alert.alertViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.*
import com.example.weatherapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(var repository : Repository)  : ViewModel() {

    private var _alert= MutableStateFlow<AlertState>(AlertState.Loading)
    val alert = _alert.asStateFlow()

    init {
        getAlerts()

    }
    private fun getAlerts(){
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllAlerts()
                ?.catch {
                    _alert.value= AlertState.Failure(it)
                }?.collect{
                    _alert.value= AlertState.Success(it)
                }
        }
    }

    fun insertAlert(alert: LocalAlert){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: LocalAlert){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAlert(alert)
            getAlerts()
        }
    }

    private fun checkConnectivity():Boolean{
        return repository.checkForInternet()
    }


}