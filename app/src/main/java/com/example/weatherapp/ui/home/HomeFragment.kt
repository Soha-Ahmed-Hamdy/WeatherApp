package com.example.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.data.utils.ApiState
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val PERMISSION_ID=40
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var fact: FactoryHomeWeather
    lateinit var dayAdapter: DayAdapter
    lateinit var hourAdapter: HourAdapter
    lateinit var homeViewModel: HomeViewModel
    private lateinit var progressIndicator: LottieAnimationView
    lateinit var countDownTime: TextView
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var lat : Double = 30.6210725
    var long : Double = 32.2687095

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        progressIndicator=binding.indicator
        countDownTime = binding.tvIndicator

        var repository=Repository.getRepositoryInstance(requireActivity().application)

        fact= FactoryHomeWeather(repository)
        homeViewModel= ViewModelProvider(requireActivity(),fact).get(HomeViewModel::class.java)
        homeViewModel.getRootHome()
        homeViewModel.getStates()
        Utility.checkUnit()


        lifecycleScope.launch {
            homeViewModel.rootWeather.collectLatest {
                when (it){
                    is ApiState.Loading->{
                        disableViews()
                        delay(3000)
                    }
                    is ApiState.Success->{

                        if (SharedPrefData.language== Utility.Language_EN_Value){
                            countDownTime.text = "Finished !!"
                        }else{
                            countDownTime.text = "تم التحميل"
                        }

                        delay(10)

                        progressIndicator.visibility = View.GONE
                        binding.tvIndicator.visibility = View.GONE

                        dayAdapter = DayAdapter(it.weatherRoot!!.daily)
                        binding.recyclerDays.adapter=dayAdapter

                        hourAdapter = HourAdapter(it.weatherRoot.hourly)
                        binding.recyclerHour.adapter=hourAdapter

                        if(SharedPrefData.language == Utility.Language_EN_Value){
                            binding.humidityMeasure.text=it.weatherRoot.current!!.humidity.toString()+" %"
                            binding.cloudMeasure.text=it.weatherRoot.current.clouds.toString()+" %"
                            binding.windMeasure.text=it.weatherRoot.current.windSpeed.toString()+" m/s"
                            binding.pressureMeasure.text=it.weatherRoot.current.pressure.toString()+" hpa"
                            binding.violateMeasure.text=it.weatherRoot.current.uvi.toString()
                            binding.visibilityMeasure.text=it.weatherRoot.current.visibility.toString()+" m"
                            binding.todayTemp.text=it.weatherRoot.current.temp.toInt().toString()+ Utility.checkUnit()
                            binding.todayImg.setImageResource(Utility.getWeatherStatusIcon(it.weatherRoot.current.weather[0].icon))
                            binding.weatherMood.text=it.weatherRoot.current.weather[0].description
                            binding.cityName.text=it.weatherRoot.timezone

                        }
                        else if(SharedPrefData.language == Utility.Language_AR_Value){
                            binding.humidityMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot!!.current!!.humidity)+"٪ "
                            binding.cloudMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot.current!!.clouds)+"٪ "
                            binding.windMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot.current.windSpeed)+" م/ث"
                            binding.pressureMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot.current.pressure)+" هيكتوباسكال"
                            binding.violateMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot.current.uvi)
                            binding.visibilityMeasure.text = Utility.convertNumbersToArabic(it.weatherRoot.current.visibility)+" م"
                            binding.todayTemp.text=
                                Utility.convertNumbersToArabic(it.weatherRoot.current.temp.toInt())+ Utility.checkUnit()
                            binding.todayImg.setImageResource(Utility.getWeatherStatusIcon(it.weatherRoot.current.weather[0].icon))
                            binding.weatherMood.text = it.weatherRoot.current.weather[0].description
                            binding.cityName.text=it.weatherRoot.timezone

                        }

                        delay(5)
                        initViews()

                    }
                    is ApiState.Failure->{
                        Toast.makeText(requireContext(),"Failure ${it.msg}", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
        return root
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
        mLocationRequest.setInterval(300000)
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
            lat= mLastLocation?.latitude!!
            long=mLastLocation?.longitude!!
            if(SharedPrefData.location== Utility.GPS){
                saveLatLong(lat.toLong(),long.toLong())
            }

            mFusedLocationClient.removeLocationUpdates(this)

        }
    }
    fun disableViews(){

        binding.moreDetailsCard.visibility = View.GONE
        binding.todayForcastCard.visibility = View.GONE

    }
    fun initViews(){
        binding.moreDetailsCard.visibility = View.VISIBLE
        binding.todayForcastCard.visibility = View.VISIBLE

    }
    fun saveLatLong(lat : Long,long:Long){
        Utility.saveLatitudeToSharedPref(requireContext(), Utility.LATITUDE_KEY,lat)
        Utility.saveLongitudeToSharedPref(requireContext(), Utility.LONGITUDE_KEY, long)

    }

}