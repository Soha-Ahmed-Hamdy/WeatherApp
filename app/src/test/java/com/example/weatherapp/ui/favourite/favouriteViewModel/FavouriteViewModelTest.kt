package com.example.weatherapp.ui.favourite.favouriteViewModel

import android.os.Bundle
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.R
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.model.Root
import com.example.weatherapp.data.repository.FakeRepository
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.repository.RepositoryInterface
import com.example.weatherapp.data.utils.RoomState
import com.example.weatherapp.ui.favourite.favAdapters.FavouritAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest {

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
    private lateinit var favouriteViewModel: FavouriteViewModel


    @Before
    fun setUp() {
        repository = FakeRepository(
            favList,
            alertList,
            rootList[0]

        )
        favouriteViewModel = FavouriteViewModel(repository)


    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getFavWeather_getItems_sameFakeData() = runBlockingTest {
        //When
        favouriteViewModel.getFav()
         var data:List<FavouritePlace> = emptyList()
        val result = favouriteViewModel.favWeather.first()

        when (result) {
            is RoomState.Loading -> {

            }
            is RoomState.Success -> {

                data = result.countrysFav
            }
            is RoomState.Failure -> {

            }
        }
    //Then
    MatcherAssert.assertThat(data.size,`is`(4) )
}

@Test
fun insertFav_item_checkSize() = runTest {
    //When
    favouriteViewModel.insertFav(favList[0])
    var data:List<FavouritePlace> = emptyList()
    val result = favouriteViewModel.favWeather.first()
    when (result) {
        is RoomState.Loading -> {

        }
        is RoomState.Success -> {

            data = result.countrysFav
        }
        is RoomState.Failure -> {

        }
    }

    //Then
    MatcherAssert.assertThat(favList.size, `is`(5))
}

@Test
fun deleteFav_deleteItem_checkSize() = runBlockingTest{
    //When
    favouriteViewModel.deleteFav(favList[0])
    var data:List<FavouritePlace> = emptyList()
    val result = favouriteViewModel.favWeather.first()
    val msg:String
    when (result) {
        is RoomState.Loading -> {

        }
        is RoomState.Success -> {

            data = result.countrysFav
        }
        is RoomState.Failure -> {
            msg= result.msg.toString()
        }
    }

    //Then
    assertThat(data.size, `is`(3))
}
}