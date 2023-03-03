package com.example.weatherapp.ui.favouriteDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.databinding.FragmentFavDetailsBinding
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.repository.Repository
import com.example.weatherapp.ui.favDetails.favDetailsViewModel.FactoryFavDetails
import com.example.weatherapp.ui.favDetails.favDetailsViewModel.FavouriteDetailsViewModel
import com.example.weatherapp.ui.Utility
import com.example.weatherapp.ui.home.homeAdapters.DayAdapter
import com.example.weatherapp.ui.home.homeAdapters.HourAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavDetailsFragment : Fragment() {

    private var _binding: FragmentFavDetailsBinding? = null
    lateinit var fact: FactoryFavDetails
    lateinit var dayAdapter: DayAdapter
    private lateinit var dayList: List<Daily?>
    lateinit var hourAdapter: HourAdapter
    private lateinit var hourList: List<Current?>
    lateinit var currentDayWeather: Current
    lateinit var lat:String
    lateinit var favItemData: FavouritePlace
    lateinit var favDetailsViewModel: FavouriteDetailsViewModel
    private lateinit var progressIndicator:LottieAnimationView
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
        progressIndicator=binding.indicator
        countDownTime = binding.tvIndicator
        fact= FactoryFavDetails(requireContext(),favItemData.lat,favItemData.lon)
        favDetailsViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteDetailsViewModel::class.java)
        favDetailsViewModel.getFavRootDetails(requireContext(),favItemData.lat,favItemData.lon)


        //lottie : https://assets6.lottiefiles.com/packages/lf20_ahY2hu.json

        lifecycleScope.launch {
            favDetailsViewModel.stateFlow.collectLatest {
                when (it){
                    is ApiState.Loading->{

                        disableViews()
                        delay(3000)

                    }
                    is ApiState.Success->{

                        if (Repository.language==Utility.Language_EN_Value){
                            countDownTime.text = "Finished !!"
                        }else{
                            countDownTime.text = "تم التحميل"
                        }
                        delay(10)

                        progressIndicator.visibility = View.GONE
                        binding.tvIndicator.visibility = View.GONE

                        dayAdapter = DayAdapter(it.weatherRoot!!.daily)
                        binding.recyclerDays.adapter=dayAdapter

                        hourAdapter = HourAdapter(it.weatherRoot!!.hourly)
                        binding.recyclerHour.adapter=hourAdapter

                        currentDayWeather=it.weatherRoot!!.current!!

                        if(Repository.language == Utility.Language_EN_Value){
                            binding.humidityMeasure.text=currentDayWeather.humidity.toString()+" %"
                            binding.cloudMeasure.text=currentDayWeather.clouds.toString()+" %"
                            binding.windMeasure.text=currentDayWeather.windSpeed.toString()+" m/s"
                            binding.pressureMeasure.text=currentDayWeather.pressure.toString()+" hpa"
                            binding.violateMeasure.text=currentDayWeather.uvi.toString()
                            binding.visibilityMeasure.text=currentDayWeather.visibility.toString()+" m"
                            binding.todayTemp.text=currentDayWeather.temp.toInt().toString()+ Utility.checkUnit()
                            binding.todayImg.setImageResource(Utility.getWeatherStatusIcon(currentDayWeather.weather[0].icon))
                            binding.weatherMood.text=currentDayWeather.weather[0].description
                            binding.cityName.text=it.weatherRoot.timezone
                        }
                        else if(Repository.language == Utility.Language_AR_Value){
                            binding.humidityMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.humidity)+"٪ "
                            binding.cloudMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.clouds)+"٪ "
                            binding.windMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.windSpeed)+" م/ث"
                            binding.pressureMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.pressure)+" هيكتوباسكال"
                            binding.violateMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.uvi)
                            binding.visibilityMeasure.text=Utility.convertNumbersToArabic(currentDayWeather.visibility)+" م"
                            binding.todayTemp.text=Utility.convertNumbersToArabic(currentDayWeather.temp.toInt())+ Utility.checkUnit()
                            binding.todayImg.setImageResource(Utility.getWeatherStatusIcon(currentDayWeather.weather[0].icon))
                            binding.weatherMood.text=currentDayWeather.weather[0].description
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