package com.example.weatherapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import java.text.SimpleDateFormat
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
    private var time: Long = 0
    private var timeFinal = ""


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

            showDatePicker("start")
        }
        binding.toPicker.setOnClickListener {

            showDatePicker("end")

        }
        binding.timePicker.setOnClickListener {
            showTimepicker()
        }
        binding.saveAlert.setOnClickListener {

            if(endDate < startDate){
                makeWarning(getString(R.string.date_warning),getString(R.string.ok))
            }else if(endDate==null || startDate== null || time== null || binding.zonePicker.text==null){
                makeWarning(getString(R.string.full),getString(R.string.ok))
            }else if(binding.fromPicker.text== "" || binding.toPicker.text== "" || binding.timePicker.text== "" || binding.zonePicker.text==""){
                makeWarning(getString(R.string.full),getString(R.string.ok))
            }else{
                val formatter = SimpleDateFormat("MMM dd, yyyy - EEE hh:mm a")
                formatter.timeZone = TimeZone.getTimeZone("GMT+2")
                val startTime = formatter.parse(timeFinal)
                val utc = startTime?.time?.div(1000)

                alertViewModel.insertAlert(
                    LocalAlert(
                        utc!!,
                        endDate,
                        binding.zonePicker.text.toString(),
                        startDate,
                        lat.toDouble(),
                        long.toDouble(),
                    )
                )
                NavHostFragment.findNavController(this).popBackStack()
            }
        }
        binding.zonePicker.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.selectMapFragment2)
        }

        return root
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("MMM dd, yyyy - EEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun makeWarning(msg : String,btn:String){
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alert.setTitle(getString(R.string.warning))

        alert.setMessage(msg)
        alert.setPositiveButton(btn) {
                _: DialogInterface, _: Int -> }
        val dialog = alert.create()
        dialog.show()
    }
    private fun showTimepicker(){
        val calendar= Calendar.getInstance()
        val timePickerDialog= TimePickerDialog(
            requireActivity(),
            {timePicker, hourOfDay, minute ->

                val time= formatTime(hourOfDay,minute)
                timeFinal = timeFinal + " " + time
                binding.timePicker.text = time

            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE],false
        )
        timePickerDialog.show()
    }
    private fun showDatePicker(dateAnnotation: String){
        val calendar= Calendar.getInstance()

        val datePickerDialog= DatePickerDialog(
            requireActivity(),
            {datePicker, year, month, day->
                timeFinal= formatDate(year, month, day)
                val msg = "$day-$month-$year"


                if(dateAnnotation=="start"){
                    startDate = Utility.dateToLong(msg)
                    binding.fromPicker.text=msg

                }else{
                    endDate = Utility.dateToLong(msg)
                    binding.toPicker.text=msg
                }

            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()-1000

        datePickerDialog.show()
    }

}