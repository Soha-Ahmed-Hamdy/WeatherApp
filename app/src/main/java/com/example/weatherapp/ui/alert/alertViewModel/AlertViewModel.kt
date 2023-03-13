package com.example.weatherapp.ui.alert.alertViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.utils.AlertState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(var repository : RepositoryInterface)  : ViewModel() {

    private var _alert= MutableStateFlow<AlertState>(AlertState.Loading)
    val alert = _alert.asStateFlow()

    init {
        getAlerts()

    }
    fun getAlerts(){
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