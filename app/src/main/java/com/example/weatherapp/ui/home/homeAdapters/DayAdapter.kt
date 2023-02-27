package com.example.weatherapp.ui.home.homeAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DayItemBinding
import com.example.weatherapp.model.Daily
import com.example.weatherapp.ui.home.Utility

class DayAdapter(private val day: List<Daily>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    lateinit var binding: DayItemBinding

    inner class ViewHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DayItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.dayName.text = Utility.timeNameOfToDay(day[position].dt)
        holder.binding.dayTemp.text =
            day[position].temp.max.toInt().toString()+
                    "/" +
                    day[position].temp.min.toInt().toString() + " °C"
        holder.binding.dayStatus.text = day[position].weather.get(0).description
        holder.binding.statusImg.setImageResource(Utility.getWeatherStatusIcon(day[position].weather[0].icon))


    }

    override fun getItemCount(): Int = day.size
}
