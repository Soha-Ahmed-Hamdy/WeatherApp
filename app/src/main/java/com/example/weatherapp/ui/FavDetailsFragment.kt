package com.example.weatherapp.ui

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavDetailsBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.model.Root
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
import com.example.weatherapp.ui.home.PERMISSION_ID
import com.example.weatherapp.ui.home.Utility
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import com.google.android.gms.location.*


class FavDetailsFragment : Fragment() {

    private var _binding: FragmentFavDetailsBinding? = null
    lateinit var fact: FactoryHomeWeather
    lateinit var dayAdapter: DayAdapter
    lateinit var dayList: List<Daily?>
    lateinit var hourAdapter: HourAdapter
    lateinit var hourList: List<Current?>
    lateinit var currentDayWeather: Current
    lateinit var lat:String
    lateinit var favItemData: FavouritePlace
    lateinit var homeViewModel: HomeViewModel


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments.let {
            favItemData= it?.getSerializable("favItem") as FavouritePlace

        }


        fact= FactoryHomeWeather(requireContext(),favItemData.lat,favItemData.lon)
        homeViewModel= ViewModelProvider(requireActivity(),fact).get(HomeViewModel::class.java)

        _binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root



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

}

