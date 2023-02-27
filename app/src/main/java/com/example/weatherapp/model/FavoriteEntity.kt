package com.example.weatherapp.model
import androidx.room.*


//@Entity
//data class FavouritePlace (
//
//    val lat: Double,
//    val lon: Double,
//    val name: String,
//    val city: String
//
//    ) : java.io.Serializable{
//
//    @PrimaryKey(autoGenerate = true)
//    var favID: Int = 0
//
//}
//
//data class RootWithCurrentList(
//    @Embedded val root:RootData,
//    @Relation(
//        parentColumn = "id_root",
//        entityColumn = "id_current")
//    val hourly: List<CurrentData>
//)
//
//data class RootWithDailyList(
//    @Embedded val root:RootData,
//    @Relation(
//        parentColumn = "id_root",
//        entityColumn = "id_daily")
//    val daily: List<DailyData>
//)
//
//data class RootWithCurrent(
//    @Embedded val root:RootData,
//    @Relation(
//        parentColumn = "id_root",
//        entityColumn = "id_current")
//    val currentData: CurrentData
//)
//
//data class DailyWithWeather(
//    @Embedded val dailyData:DailyData,
//    @Relation(
//        parentColumn = "id_daily",
//        entityColumn = "id_weather")
//    val weatherData: List<WeatherData>
//)
//
//data class CurrentWithWeather(
//    @Embedded val currentData: CurrentData,
//    @Relation(
//        parentColumn = "id_current",
//        entityColumn = "id_weather")
//    val weather: List<WeatherData>
//)
//
//data class DailyWithTemp(
//    @Embedded val root:DailyData,
//    @Relation(
//        parentColumn = "id_daily",
//        entityColumn = "id_temp")
//    val tempData: TempData
//)
//
//
//@Entity
//data class RootData (
//
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("id_root")
//    val id:Int,
//    val lat: Double,
//    val lon: Double,
//    val timezone: String,
//    val timezoneOffset: Long,
//
//)
//
//
//@Entity
//data class CurrentData (
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("id_current")
//    val id: Int,
//    val dt: Long,
//    val sunrise: Long? = null,
//    val sunset: Long? = null,
//    val temp: Double,
//    val feelsLike: Double,
//    val pressure: Long,
//    val humidity: Long,
//    val dewPoint: Double,
//    val uvi: Double,
//    val clouds: Long,
//    val visibility: Long,
//    val windSpeed: Double,
//    val windDeg: Long,
//    val windGust: Double,
//    val pop: Long? = null
//)
//
//@Entity
//data class DailyData (
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("id_daily")
//    val id:Int,
//    val dt: Long,
//    val sunrise: Long,
//    val sunset: Long,
//    val moonrise: Long,
//    val moonset: Long,
//    val moonPhase: Double,
//    val pressure: Long,
//    val humidity: Long,
//    val dewPoint: Double,
//    val windSpeed: Double,
//    val windDeg: Long,
//    val windGust: Double,
//    val clouds: Long,
//    val pop: Long,
//    val uvi: Double
//)
//
//@Entity
//data class WeatherData (
//    @PrimaryKey
//    @ColumnInfo("id_weather")
//    val id: Long,
//    val description: String,
//    val icon: String
//)
//@Entity
//data class TempData (
//    @PrimaryKey
//    @ColumnInfo("id_temp")
//    val id: Long,
//    val day: Double,
//    val min: Double,
//    val max: Double,
//    val night: Double,
//    val eve: Double,
//    val morn: Double
//)
//
//
