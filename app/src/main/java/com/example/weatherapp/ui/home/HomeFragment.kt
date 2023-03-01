package com.example.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import com.google.android.gms.location.*

const val PERMISSION_ID=40
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var fact: FactoryHomeWeather
    lateinit var dayAdapter: DayAdapter
    lateinit var dayList: List<Daily?>
    lateinit var hourAdapter: HourAdapter
    lateinit var hourList: List<Current?>
    lateinit var currentDayWeather: Current

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var lat:String
    lateinit var long:String
    lateinit var streetName:String

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fact= FactoryHomeWeather(requireContext(),30.62108,32.2687725)
        var homeViewModel= ViewModelProvider(requireActivity(),fact).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        homeViewModel.dailyList.observe(viewLifecycleOwner) {
            dayList=it
            dayAdapter=
                DayAdapter(dayList as List<Daily>)
            binding.recyclerDays.adapter=dayAdapter
            }
        homeViewModel.hourlyList.observe(viewLifecycleOwner) {
            hourList=it
            hourAdapter=
                HourAdapter(hourList as List<Current>)
            binding.recyclerHour.adapter=hourAdapter

        }
        homeViewModel.currentWeather.observe(viewLifecycleOwner) {
            currentDayWeather=it

            binding.humidityMeasure.text=currentDayWeather.humidity.toString()+" %"
            binding.cloudMeasure.text=currentDayWeather.clouds.toString()+" %"
            binding.windMeasure.text=currentDayWeather.windSpeed.toString()+" m/s"
            binding.pressureMeasure.text=currentDayWeather.pressure.toString()+" hpa"
            binding.violateMeasure.text=currentDayWeather.uvi.toString()
            binding.visibilityMeasure.text=currentDayWeather.visibility.toString()+" m"
            binding.todayTemp.text=currentDayWeather.temp.toInt().toString()+ " °C"
            binding.todayImg.setImageResource(Utility.getWeatherStatusIcon(currentDayWeather.weather[0].icon))
            binding.weatherMood.text=currentDayWeather.weather[0].description

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION_ID){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation():Unit{
        if (checkPermission()){
            if (isLocationEnabled()){
                requestNewLocationData()
            }else{
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback: LocationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation : Location? =locationResult.getLastLocation()
            lat=mLastLocation?.latitude.toString()
            long=mLastLocation?.longitude.toString()

        }
    }

}