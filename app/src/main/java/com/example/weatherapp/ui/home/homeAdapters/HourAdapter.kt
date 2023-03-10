package com.example.weatherapp.ui.home.homeAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.utils.Utility

class HourAdapter(
    private val hour: List<Current>
) : RecyclerView.Adapter<HourAdapter.ViewHolder>() {
    lateinit var binding: HourItemBinding

    inner class ViewHolder(var binding:HourItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=HourItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(SharedPrefData.language == Utility.Language_EN_Value){
            holder.binding.timeTemp.text =
                hour[position].temp.toInt().toString()+ Utility.checkUnit()
            holder.binding.currentTime.text = Utility.timeStampToHour(hour[position].dt)
            holder.binding.timeIcon.setImageResource(Utility.getWeatherStatusIcon(hour[position].weather[0].icon))
        }else{
            holder.binding.timeTemp.text =
                Utility.convertNumbersToArabic(hour[position].temp.toInt())+ Utility.checkUnit()
            holder.binding.currentTime.text = Utility.timeStampToHour(hour[position].dt)
            holder.binding.timeIcon.setImageResource(Utility.getWeatherStatusIcon(hour[position].weather[0].icon))
        }



    }

    override fun getItemCount(): Int = hour.size
}
