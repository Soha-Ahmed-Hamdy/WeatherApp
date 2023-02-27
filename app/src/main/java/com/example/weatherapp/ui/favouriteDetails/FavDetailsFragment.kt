package com.example.weatherapp.ui.favouriteDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.databinding.FragmentFavDetailsBinding
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
import com.example.weatherapp.ui.home.Utility
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import com.google.android.gms.location.*


class FavDetailsFragment : Fragment() {

    private var _binding: FragmentFavDetailsBinding? = null
    lateinit var fact: FactoryFavDetails
    lateinit var dayAdapter: DayAdapter
    lateinit var dayList: List<Daily?>
    lateinit var hourAdapter: HourAdapter
    lateinit var hourList: List<Current?>
    lateinit var currentDayWeather: Current
    lateinit var lat:String
    lateinit var favItemData: FavouritePlace
    lateinit var favDetailsViewModel: FavouriteDetailsViewModel


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments.let {
            favItemData= it?.getSerializable("favItem") as FavouritePlace

        }

        fact= FactoryFavDetails(requireContext(),favItemData.lat,favItemData.lon)
        favDetailsViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteDetailsViewModel::class.java)

        _binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        favDetailsViewModel.favDetailsWeather.observe(viewLifecycleOwner) {
            if(it!=null){

                dayList=it.daily
                dayAdapter=
                    DayAdapter(dayList as List<Daily>)
                binding.recyclerDays.adapter=dayAdapter

                hourList=it.hourly
                hourAdapter=
                    HourAdapter(hourList as List<Current>)
                binding.recyclerHour.adapter=hourAdapter

                currentDayWeather=it.current!!

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

