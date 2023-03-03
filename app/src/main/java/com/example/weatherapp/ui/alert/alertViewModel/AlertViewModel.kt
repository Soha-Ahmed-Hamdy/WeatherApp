package com.example.weatherapp.ui.alert.alertViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.*
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(context: Context)  : ViewModel() {

    var repo = Repository

    private var _alert= MutableStateFlow<AlertState>(AlertState.Loading)
    val alert = _alert.asStateFlow()



    init {
        getAlerts(context)

    }
    fun getAlerts(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            repo.getAllAlerts(context)
                ?.catch {
                    _alert.value= AlertState.Failure(it)
                }?.collect{
                    _alert.value= AlertState.Success(it)
                }
        }
    }

    fun insertAlert(context: Context, alert: LocalAlert){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertAlert(context,alert)
        }
    }

    fun deleteAlert(context: Context, alert: LocalAlert){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteAlert(context,alert)
            getAlerts(context)
        }
    }

    fun checkConnectivity(context: Context):Boolean{
        return repo.checkForInternet(context)
    }


}