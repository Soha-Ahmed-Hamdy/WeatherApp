package com.example.weatherapp.ui.alert.alertViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.repository.FakeRepository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.utils.AlertState
import com.example.weatherapp.data.utils.RoomState
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private var favList: MutableList<FavouritePlace> = mutableListOf<FavouritePlace>(
        FavouritePlace(56.12, 12.12, "", ""),
        FavouritePlace(58.12, 15.12, "", ""),
        FavouritePlace(59.12, 16.12, "", ""),
        FavouritePlace(52.12, 19.12, "", "")
    )
    private var alertList: MutableList<LocalAlert> = mutableListOf<LocalAlert>(
        LocalAlert(133, 465, "", 586),
        LocalAlert(153, 1655, "", 1235),
        LocalAlert(193, 988, "", 9612),
        LocalAlert(132, 3215, "", 3245)

    )
    private var rootList: MutableList<Root> = mutableListOf<Root>(
        Root(46, 65.0, 54.0, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList())
    )
    private lateinit var repository: RepositoryInterface
    private lateinit var alertViewModel: AlertViewModel


    @Before
    fun setUp() {
        repository = FakeRepository(
            favList,
            alertList,
            rootList[0]

        )
        alertViewModel = AlertViewModel(repository)


    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAlert_checkSizeOfList() = runBlockingTest{
        //When
        alertViewModel.getAlerts()
        var data:List<LocalAlert> = emptyList()
        val result = alertViewModel.alert.first()

        when (result) {
            is AlertState.Loading -> {

            }
            is AlertState.Success -> {

                data = result.alertsData
            }
            is AlertState.Failure -> {

            }
        }
        //Then
        MatcherAssert.assertThat(data.size, CoreMatchers.`is`(4))
    }

    @Test
    fun insertAlert_addItemToList_checkListSize() = runBlockingTest{
        //When
        alertViewModel.insertAlert(alertList[0])
        var data:List<LocalAlert> = emptyList()
        val result = alertViewModel.alert.first()

        when (result) {
            is AlertState.Loading -> {

            }
            is AlertState.Success -> {

                data = result.alertsData
            }
            is AlertState.Failure -> {

            }
        }
        //Then
        MatcherAssert.assertThat(data.size, `is`(5))
    }

    @Test
    fun deleteAlert_delItem_CheckListSize() = runBlockingTest {
        //When
        alertViewModel.deleteAlert(alertList[0])
        var data:List<LocalAlert> = emptyList()
        val result = alertViewModel.alert.first()

        when (result) {
            is AlertState.Loading -> {

            }
            is AlertState.Success -> {

                data = result.alertsData
            }
            is AlertState.Failure -> {

            }
        }
        //Then
        MatcherAssert.assertThat(data.size, `is`(3))
    }

}