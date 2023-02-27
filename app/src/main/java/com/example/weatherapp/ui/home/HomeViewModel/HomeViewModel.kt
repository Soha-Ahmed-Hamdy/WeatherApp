package com.example.weatherapp.ui.home.HomeViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.Root
import com.example.weatherapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(context: Context,lat: Double,long: Double) : ViewModel() {

    var repo = Repository

    private var _dailyList=MutableLiveData<List<Daily>>()
    val dailyList:LiveData<List<Daily>> = _dailyList

    private var _hourlyList=MutableLiveData<List<Current>>()
    val hourlyList:LiveData<List<Current>> = _hourlyList

    private var _currentWeather=MutableLiveData<Current>()
    val currentWeather:LiveData<Current> = _currentWeather


    init {
        getDaily(context ,lat ,long)
        getHourly(context ,lat ,long)
        getCurrent(context ,lat ,long)
    }

    fun getDaily(context: Context,lat: Double,long: Double){
        viewModelScope.launch (Dispatchers.IO){
            _dailyList.postValue(repo.getHomeData(context,lat,long)?.daily)
        }
    }
    fun getHourly(context: Context,lat: Double,long: Double){
        viewModelScope.launch (Dispatchers.IO){
            _hourlyList.postValue(repo.getHomeData(context,lat,long)?.hourly)
        }
    }

    fun getCurrent(context: Context,lat: Double,long: Double){
        viewModelScope.launch (Dispatchers.IO){
            _currentWeather.postValue(repo.getHomeData(context,lat,long)?.current!!)
        }
    }


    fun insertRootToLocal(context: Context, root: Root){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertRoot(context,root)
        }
    }

    fun deleteLocalHomeData(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteHomeData(context)
        }
    }

    private val _text = MutableLiveData<String>().apply {

        value = dailyList.value?.get(0)?.temp.toString()
    }
    val text: LiveData<String> = _text
}