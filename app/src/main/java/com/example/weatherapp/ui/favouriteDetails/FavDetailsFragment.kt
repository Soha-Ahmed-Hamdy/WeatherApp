package com.example.weatherapp.ui.favouriteDetails

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.databinding.FragmentFavDetailsBinding
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
import com.example.weatherapp.ui.home.Utility
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import com.google.android.gms.location.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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
    lateinit var progressIndicator:LottieAnimationView
    lateinit var countDownTime: TextView


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arguments.let {
            favItemData= it?.getSerializable("favItem") as FavouritePlace

        }

        fact= FactoryFavDetails(requireContext(),favItemData.lat,favItemData.lon)
        favDetailsViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteDetailsViewModel::class.java)
        favDetailsViewModel.getFavRootDetails(requireContext(),favItemData.lat,favItemData.lon)




        lifecycleScope.launch {
            progressIndicator=binding.indicator
            countDownTime = binding.tvIndicator
            favDetailsViewModel.stateFlow.collectLatest {
                when (it){
                    is ApiState.Loading->{
                        delay(50)
                        disableViews()
                        countDownTime.text = "Loading..!!"
                    }
                    is ApiState.Success->{

                        countDownTime.text = "Finished!!"
                        delay(5)
                        initViews()
                        progressIndicator.visibility = View.GONE
                        binding.tvIndicator.visibility = View.GONE


                        dayList=it.weatherRoot!!.daily
                        dayAdapter = DayAdapter(dayList as List<Daily>)
                        binding.recyclerDays.adapter=dayAdapter

                        hourList=it.weatherRoot!!.hourly
                        hourAdapter = HourAdapter(hourList as List<Current>)
                        binding.recyclerHour.adapter=hourAdapter

                        currentDayWeather=it.weatherRoot!!.current!!

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
                    is ApiState.Failure->{
                        Toast.makeText(requireContext(),"Failure $it", Toast.LENGTH_LONG).show()

                    }
                }
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
    /*fun startProgressIndicator(intcountTimeDown:Int){

        disableViews()
        val lngCountDownTime = intcountTimeDown.toLong()
        countDownTimer=object :CountDownTimer(lngCountDownTime, 1000){

            var progress = 0
            override fun onTick(millisUntilFinished: Long) {
                var totalTime = lngCountDownTime/1000
                var timeLeft = millisUntilFinished/1000
                progress= (timeLeft * 100 / totalTime).toInt()
                progressIndicator.progress = progress
                countDownTime.text = "Loading..!!"
            }

            override fun onFinish() {
                countDownTime.text = "Finished!!"
            }

        }.start()

    }*/
    fun disableViews(){

        binding.moreDetailsCard.visibility = View.GONE
        binding.todayForcastCard.visibility = View.GONE

    }

    fun initViews(){
        binding.moreDetailsCard.visibility = View.VISIBLE
        binding.todayForcastCard.visibility = View.VISIBLE

    }


}

