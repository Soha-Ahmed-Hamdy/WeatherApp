package com.example.weatherapp.ui.alert.alertAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertSavedItemBinding
import com.example.weatherapp.model.LocalAlert
import com.example.weatherapp.model.Utility

class AlertAdapter(private val alerts: List<LocalAlert>
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
        holder.binding.fromMeasure.text = Utility.longToDate(alerts[position].start)
        holder.binding.toMeasure.text = Utility.longToDate(alerts[position].end)
        holder.binding.timeMeasure.text = Utility.timeStampToHour(alerts[position].time)
        holder.binding.zoneMeasure.text = alerts[position].zoneName
        holder.binding.delIcon.setOnClickListener { alerts[position].let { listener(it) } }

    }

    override fun getItemCount(): Int = alerts.size
}