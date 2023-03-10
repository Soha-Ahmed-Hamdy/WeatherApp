package com.example.weatherapp.database


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.database.AlertDAO
import com.example.weatherapp.data.database.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDAOTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var alertDao: AlertDAO

    @Before
    fun setUp() {

        //initialize database
        db= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        alertDao= db.alertDAO()!!
    }

    @After
    fun tearDown() {

        //close database
        db.close()

    }

    @Test
    fun allAlerts() = runBlockingTest{

        val data = LocalAlert(12323,13652,"",1365,12.21,12.45)
        val data1 = LocalAlert(12365,13652,"",1265,10.21,12.45)
        val data2 = LocalAlert(15323,10652,"",165,16.21,12.45)
        val data3 = LocalAlert(16323,10652,"",135,19.21,12.45)

        alertDao.insertAlert(data)
        alertDao.insertAlert(data1)
        alertDao.insertAlert(data2)
        alertDao.insertAlert(data3)

        //When
        val result = alertDao.allAlerts()
            .first()

        //Then
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(4))
    }

    @Test
    fun insertFavourite_insertOneItem_returnTheItem() = runBlockingTest {
        //Given
        val data = LocalAlert(16323,10652,"",135,19.21,12.45)

        //When
        alertDao.insertAlert(data)

        //Then
        val result= alertDao.allAlerts().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())

    }

    @Test
    fun insertAlert() {
    }

    @Test
    fun deleteAlert() {
    }
}