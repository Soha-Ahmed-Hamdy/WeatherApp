package com.example.weatherapp.ui.alert.alertAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertSavedItemBinding
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.data.utils.Utility.Companion.dayConverterToString
import com.example.weatherapp.data.utils.Utility.Companion.timeConverterToString

class AlertAdapter(val context: Context,private val alerts: List<LocalAlert>
                      , var listener: (LocalAlert) -> Unit

) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    lateinit var binding: AlertSavedItemBinding
    inner class ViewHolder(var binding: AlertSavedItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertSavedItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.fromMeasure.text = alerts[position].start?.let { Utility.longToDate(it) }
        holder.binding.toMeasure.text = alerts[position].end?.let { Utility.longToDate(it) }
        holder.binding.timeMeasure.text = alerts[position].time?.let { Utility.timeStampToHour(it) }
        holder.binding.zoneMeasure.text = alerts[position].zoneName
        holder.binding.delIcon.setOnClickListener { alerts[position].let { listener(it) }
//            holder.binding.fromMeasure.text = "${ dayConverterToString(alerts[position].start,context) }   "
//            holder.binding.timeMeasure.text = "${ timeConverterToString(alerts[position].start,context) } "
//            holder.binding.toMeasure.text = "${ dayConverterToString(alerts[position].end,context) }"

        }

    }

    override fun getItemCount(): Int = alerts.size
}