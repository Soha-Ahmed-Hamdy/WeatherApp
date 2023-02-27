package com.example.weatherapp.ui.favourite.favAdapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.ui.favourite.FavouriteFragment
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class FavouritAdapter(private val locations: List<FavouritePlace?>
                      , var listener: (FavouritePlace) -> Unit

) : RecyclerView.Adapter<FavouritAdapter.ViewHolder>() {

    lateinit var binding: FavItemBinding
    inner class ViewHolder(var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

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
        holder.binding.allView.setOnClickListener {
            var bundle :Bundle?= Bundle()
            bundle?.putSerializable("favItem",locations[position])
            Navigation.findNavController(it).navigate(R.id.favDetailsFragment,bundle)

        }

    }

    override fun getItemCount(): Int = locations.size
}
