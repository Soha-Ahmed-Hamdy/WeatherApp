package com.example.weatherapp.ui.favourite

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavouriteBinding
import com.example.weatherapp.model.FavouritePlace
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favAdapters.FavouritAdapter
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    lateinit var favList: List<FavouritePlace?>
    lateinit var favAdapter: FavouritAdapter
    lateinit var fact: FactoryFavouriteWeather


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fact= FactoryFavouriteWeather(requireContext())
        var favouriteViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteViewModel::class.java)

        binding.floatingAddFav.setOnClickListener {
            if(favouriteViewModel.checkConnectivity(requireContext())){
                Navigation.findNavController(root).navigate(R.id.action_nav_favourite_to_mapFragment)
            }else{
                displayDialog()
            }

        }

        favouriteViewModel.favWeather.observe(viewLifecycleOwner) {
            favList=it
            var listener={
                    favPlace: FavouritePlace ->
                favouriteViewModel.deleteFav(requireContext(),favPlace)
            }
            var listener2={
                    favPlace: FavouritePlace ->
                    if(favouriteViewModel.checkConnectivity(requireContext())){

                        var bundle :Bundle?= Bundle()
                        bundle?.putSerializable("favItem",favPlace)
                        Navigation.findNavController(root).navigate(R.id.favDetailsFragment,bundle)
                    }else{
                   displayDialog()
                }
            }
            favAdapter=
                FavouritAdapter(favList,listener,listener2)

            binding.favRecycler.adapter=favAdapter

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun displayDialog(){
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alert.setTitle("Warning")
        alert.setMessage("Check Internet Connection")
        alert.setPositiveButton("Cancel") {
                _: DialogInterface, _: Int ->


            Toast.makeText(requireContext()
                , "cancelling"
                , Toast.LENGTH_SHORT).show()
        }
        val dialog = alert.create()
        dialog.show()
    }

}