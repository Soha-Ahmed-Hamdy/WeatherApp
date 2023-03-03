package com.example.weatherapp.ui.alert.alertAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertSavedItemBinding
import com.example.weatherapp.model.LocalAlert
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
        holder.binding.fromMeasure.text = alerts[position].start.toString()
        holder.binding.toMeasure.text = alerts[position]?.end!!.toString()
        holder.binding.timeMeasure.text = alerts[position].time.toString()
        holder.binding.zoneMeasure.text = alerts[position].zoneName
        holder.binding.allViewAlert.setOnClickListener { alerts[position]?.let { it -> listener(it) } }

    }

    override fun getItemCount(): Int = alerts.size
}