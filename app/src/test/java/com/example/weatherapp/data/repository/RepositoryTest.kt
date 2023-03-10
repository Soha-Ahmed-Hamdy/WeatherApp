package com.example.weatherapp.data.repository

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private var favList:MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(
        FavouritePlace(56.12,12.12,"",""),
        FavouritePlace(58.12,15.12,"",""),
        FavouritePlace(59.12,16.12,"",""),
        FavouritePlace(52.12,19.12,"","")
    )

    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(
        LocalAlert(133,465,"",586),
        LocalAlert(153,1655,"",1235),
        LocalAlert(193,988,"",9612),
        LocalAlert(132,3215,"",3245)

    )
    private var rootList: MutableList<Root> = mutableListOf<Root>(
        Root(46,655.0,584.0,"asdjadsk",565,null, emptyList(), emptyList(), emptyList())
    )

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository

    @Before
    fun initialization(){
        remoteDataSource= FakeDataSource(favList,alertList,rootList)
        localDataSource= FakeDataSource(favList,alertList,rootList)
        repository = Repository(remoteDataSource,localDataSource,ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getStates() {
    }

    @Test
    fun getFavDetails() {
    }

    @Test
    fun getHomeData() {
    }

    @Test
    fun insertFavCountry() {
    }

    @Test
    fun deleteFavCountry() {
    }

    @Test
    fun getAllFavCountry() {
    }

    @Test
    fun insertAlert() {
    }

    @Test
    fun deleteAlert() {
    }

    @Test
    fun getAllAlerts() {
    }

    @Test
    fun checkForInternet() {
    }
}