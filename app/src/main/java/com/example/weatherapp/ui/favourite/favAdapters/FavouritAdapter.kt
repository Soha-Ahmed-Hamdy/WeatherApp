package com.example.weatherapp.ui.favourite.favAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.model.FavouritePlace

class FavouritAdapter(private val locations: List<FavouritePlace?>
                      , var listener: (FavouritePlace) -> Unit
                      , var listener2: (FavouritePlace) -> Unit

) : RecyclerView.Adapter<FavouritAdapter.ViewHolder>() {

    lateinit var binding: FavItemBinding
    inner class ViewHolder(var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cityName.text = locations[position]?.city
        holder.binding.countryName.text = locations[position]?.name
        holder.binding.deleteLoc.setOnClickListener { locations[position]?.let { it1 -> listener(it1) } }
        holder.binding.allView.setOnClickListener { locations[position]?.let { it1 -> listener2(it1) } }

    }

    override fun getItemCount(): Int = locations.size
}
