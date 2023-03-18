package com.example.weatherapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var timeToDatabase: String
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

    private fun onClickTime() {
        binding.timePickerShow.setOnTimeChangedListener { _, hour, minute ->
            timeFinal = timeFinal + " " + formatTime(hour, minute)

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

            timeFinal = formatDate(year, month, day)


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

}