package com.example.weatherapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.floatingAddFav.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_nav_favourite_to_mapFragment)
        }


        fact= FactoryFavouriteWeather(requireContext())
        var favouriteViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteViewModel::class.java)

        favouriteViewModel.favWeather.observe(viewLifecycleOwner) {
            favList=it
//            var listener={
//                    favPlace: FavouritePlace ->
//                favouriteViewModel.deleteFav(requireContext(),favPlace)
//            }
//            var listener2={
//                    favPlace: FavouritePlace ->
//                var bundle :Bundle?= Bundle()
//                bundle?.putSerializable("favItem",favPlace)
//                Navigation.findNavController(root).navigate(R.id.favDetailsFragment,bundle)
//            }
            favAdapter=
                FavouritAdapter(favList)
                {favPlace: FavouritePlace ->
                    favouriteViewModel.deleteFav(requireContext(),favPlace)
                }
            binding.favRecycler.adapter=favAdapter

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}