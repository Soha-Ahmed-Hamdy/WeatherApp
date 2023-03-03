package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertSpecificationBinding
import java.util.*


class AlertSpecificationFragment : DialogFragment() {

    private var _binding: FragmentAlertSpecificationBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlertSpecificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
            //TODO saving in database
            NavHostFragment.findNavController(this).popBackStack()
        }



        return root
    }

    private fun onClickTime() {
        binding.timePickerShow.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = ""
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
            if (binding.timePicker != null) {
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
            val msg = "$day/$month/$year"
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
            val message = "$day/$month/$year"
            binding.toPicker.text=message
            binding.datePickerShow.visibility = ViewGroup.GONE
        }

    }
}