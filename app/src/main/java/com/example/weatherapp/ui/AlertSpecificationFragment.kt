package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertSpecificationBinding
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.ui.alert.alertViewModel.AlertViewModel
import com.example.weatherapp.ui.alert.alertViewModel.FactoryAlert
import java.util.*
import java.util.concurrent.TimeUnit


class AlertSpecificationFragment : DialogFragment() {

    private var _binding: FragmentAlertSpecificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var fact: FactoryAlert
    private var lat:Long=0
    private var long:Long=0
    private var startDate: Long =0
    private var endDate: Long=0
    private lateinit var timeToDatabase: String
    private var time: Long = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlertSpecificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val repository = Repository.getRepositoryInstance(requireActivity().application)

        fact = FactoryAlert(repository)
        alertViewModel =
            ViewModelProvider(requireActivity(), fact).get(AlertViewModel::class.java)

        binding.datePickerShow.minDate = System.currentTimeMillis()-1000


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("cityName")?.observe(
            viewLifecycleOwner) { result ->
               binding.zonePicker.text=result
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Long>("lat")?.observe(
            viewLifecycleOwner) { result ->
            lat = result

        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Long>("long")?.observe(
            viewLifecycleOwner) { result ->
            long = result

        }

        binding.fromPicker.setOnClickListener {
            binding.datePickerShow.visibility = View.VISIBLE
            onClickFromDate()
        }
        binding.toPicker.setOnClickListener {
            binding.datePickerShow.visibility = View.VISIBLE
            onClickToDate()

        }
        binding.timePicker.setOnClickListener {
            binding.timePickerShow.visibility = View.VISIBLE
            onClickTime()
        }
        binding.saveAlert.setOnClickListener {
            alertViewModel.insertAlert(
                LocalAlert(
                time,
                endDate,
                binding.zonePicker.text.toString(),
                startDate,
                lat.toDouble(),
                long.toDouble()
                )
            )
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.zonePicker.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.selectMapFragment2)
        }

        return root
    }



    private fun onClickTime() {
        binding.timePickerShow.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = ""
            time = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(hour.toLong()))
            time = time.minus(3600L * 2)
            timeToDatabase = "$hour:$minute"
            // AM_PM decider logic
            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (binding.timePicker.text != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "$hour : $min $am_pm"
                binding.timePicker.text = msg
                binding.timePickerShow.visibility = ViewGroup.GONE
            }
        }


    }
    private fun onClickFromDate(){
        val today = Calendar.getInstance()
        binding.datePickerShow.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            val msg = "$day-$month-$year"
            startDate = Utility.dateToLong(msg)
            binding.fromPicker.text=msg
            binding.datePickerShow.visibility = ViewGroup.GONE
        }

    }
    private fun onClickToDate(){
        val today = Calendar.getInstance()
        binding.datePickerShow.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            val message = "$day-$month-$year"
            endDate = Utility.dateToLong(message)
            binding.toPicker.text=message
            binding.datePickerShow.visibility = ViewGroup.GONE
        }

    }


}