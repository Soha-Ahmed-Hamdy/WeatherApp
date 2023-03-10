package com.example.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*


@Entity
data class Root (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_root")
    val id:Long,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current?=null,
    val hourly: List<Current>,
    val daily: List<Daily>,
    val alerts: List<Alert>
)


data class Current(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val pop: Double? = null
)

data class Weather (
    val id: Long,
    val main: Main,
    val description: String,
    val icon: String
)

enum class Description {
    BrokenClouds,
    ClearSky,
    FewClouds,
    OvercastClouds,
    ScatteredClouds
}

enum class Icon {
    The01D,
    The01N,
    The02D,
    The02N,
    The03D,
    The03N,
    The04N
}

enum class Main {
    Clear,
    Clouds
}

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double
)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@Entity
data class FavouritePlace (

    val lat: Double,
    val lon: Double,
    val name: String,
    val city: String

    ) : java.io.Serializable{

    @PrimaryKey(autoGenerate = true)
    var favID: Int = 0

}

data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    @SerializedName("sender_name")
    val senderName: String,
    val start: Int,
    val tags: List<String>
)

@Entity
data class LocalAlert(

    val time: Long,
    val end: Long,
    val zoneName: String,
    val start: Long,
    val lattuide: Double=0.0,
    val longtuide: Double=0.0
) : java.io.Serializable{

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}