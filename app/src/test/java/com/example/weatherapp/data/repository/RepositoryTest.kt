package com.example.weatherapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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
        Root(46,65.0,54.0,"asdjadsk",565,null, emptyList(), emptyList(), emptyList())
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

    @ExperimentalCoroutinesApi
    @Test
    fun insertFavCountry_enteringOneMoreItem_checkListSize() = runBlockingTest{
        //Given
        val item= favList[0]

        //When
        repository.insertFavCountry(item)

        //Then
        MatcherAssert.assertThat(favList.size,`is` (5))
    }

    @Test
    fun deleteFavCountry_deleteOneItem_checkListSize() = runBlockingTest{
        //Given
        val item=favList[0]

        //When
        repository.deleteFavCountry(item)

        //Then
        MatcherAssert.assertThat(favList.size,`is`(3))
    }

    @Test
    fun getAllFavCountry_checkSize() = runBlockingTest{
        //Given

        //When
        val result= repository.getAllFavCountry().first()

        //Then
        MatcherAssert.assertThat(result.size,`is`(favList.size))
    }

    @Test
    fun insertAlert_insertOneMoreItem_checkListSize() = runBlockingTest{
        //Given
        val item= alertList[0]

        //When
        repository.insertAlert(item)

        //Then
        MatcherAssert.assertThat(alertList.size,`is`(5))
    }

    @Test
    fun deleteAlert_deleteOneItem_checkListSize() = runBlockingTest{
        //Given
        val item= alertList[0]

        //When
        repository.deleteAlert(alertList[0])

        //Then
        MatcherAssert.assertThat(alertList.size,`is`(3))
    }

    @Test
    fun getAllAlerts_checkSize() = runBlockingTest{
        //Given

        //When
        val result= repository.getAllAlerts().first()

        //Then
        MatcherAssert.assertThat(result.size,`is`(alertList.size))
    }

}