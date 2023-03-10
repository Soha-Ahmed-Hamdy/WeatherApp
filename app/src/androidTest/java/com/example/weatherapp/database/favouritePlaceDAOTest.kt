package com.example.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.model.FavouritePlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class favouritePlaceDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var db:AppDatabase
    lateinit var favDao: favouritePlaceDAO

    @Before
    fun setUp() {
        //initialize database
        db= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        favDao= db.favouritePlaceDAO()!!

    }

    @After
    fun tearDown() {
        //close database
        db.close()
    }

    @Test
    fun getFavourites_insertFavouriteItems_countOfItemSame() = runBlockingTest{
        //Given
        val data = FavouritePlace(120.233,13.123,"","")
        val data1 = FavouritePlace(12.233,33.123,"","")
        val data2 = FavouritePlace(30.233,15.123,"","")
        val data3 = FavouritePlace(50.233,23.123,"","")

        favDao.insertFavourite(data)
        favDao.insertFavourite(data1)
        favDao.insertFavourite(data2)
        favDao.insertFavourite(data3)

        //When
        val result = favDao.allFavouriteWeather()
            .first()

        //Then
        MatcherAssert.assertThat(result.size,`is` (4))
    }

    @Test
    fun insertFavourite_insertOneItem_returnTheItem() = runBlockingTest {
        //Given
        val data = FavouritePlace(120.233,13.123,"","")

        //When
        favDao.insertFavourite(data)
        //Then
        val result= favDao.allFavouriteWeather().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())

    }

    @Test
    fun deleteFavouritePlace_deleteItem_checkIsNull()= runBlockingTest {

        //Given
        val data = FavouritePlace(120.233,13.123,"","")
        favDao.insertFavourite(data)
        val insertedData= favDao.allFavouriteWeather().first()
        //When
        favDao.deleteFavouritePlace(insertedData[0])
        //Then
        val result= favDao.allFavouriteWeather().first()
        assertThat(result, IsEmptyCollection.empty())
        assertThat(result.size, `is`(0))
    }
}