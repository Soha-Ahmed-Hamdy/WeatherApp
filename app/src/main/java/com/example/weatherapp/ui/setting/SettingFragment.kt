package com.example.weatherapp.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.data.utils.LocaleManager
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.Utility


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var fact: FactorySetting
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var root: View
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        root = binding.root

        repository = Repository.getRepositoryInstance(requireActivity().application)

        checkRadioButtons()
        changeLanguage()
        changeTemperature()
        changeLocation()

        return root
    }
    private fun refreshFragment(){
        requireActivity().recreate()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleManager.setLocale(context)
    }
    private fun changeLanguage(){
        binding.radioGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.english.id -> {

                    Utility.saveLanguageToSharedPref(
                        requireContext(),
                        Utility.Language_Key,
                        Utility.Language_EN_Value)
                    LocaleManager.setLocale(requireContext())
                    refreshFragment()

                }
                checkedButtonId == binding.arabic.id -> {

                Utility.saveLanguageToSharedPref(requireContext()
                    , Utility.Language_Key
                    , Utility.Language_AR_Value)
                LocaleManager.setLocale(requireContext())
                refreshFragment()
            }
            }

        }
    }

    private fun changeTemperature(){
        binding.radioGroupTemp.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.celsius.id -> {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.METRIC)
                    refreshFragment()
                }
                binding.kelvin.id -> {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.STANDARD)
                    refreshFragment()
                }
                binding.fahrenheit.id -> {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.IMPERIAL)
                    refreshFragment()
                }
            }
        }
    }

    private fun changeLocation(){
        binding.radioGroupLocation.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.map.id -> {
                    Utility.saveLocationToSharedPref(requireContext(), Utility.LOCATION_KEY, Utility.MAP)
                    Navigation.findNavController(root)
                        .navigate(R.id.locationMapFragment)
                }
                binding.gps.id -> {
                    Utility.saveLocationToSharedPref(requireContext(), Utility.LOCATION_KEY, Utility.GPS)
                }

            }
        }
    }

    private fun checkRadioButtons(){
        initViewModel()
        checkLanguage()
        checkUnit()
        checkLocation()
    }

    private fun initViewModel(){
        fact= FactorySetting(repository)
        settingViewModel= ViewModelProvider(requireActivity(),fact).get(SettingViewModel::class.java)
        settingViewModel.getStates()

    }
    private fun checkUnit(){
        when (SharedPrefData.unit) {
            Utility.IMPERIAL -> {
                binding.fahrenheit.isChecked=true
            }
            Utility.STANDARD -> {
                binding.kelvin.isChecked=true
            }
            else -> {
                binding.celsius.isChecked=true
            }
        }
    }
    private fun checkLanguage(){
        if(SharedPrefData.language== Utility.Language_EN_Value){
            binding.english.isChecked=true
        }else{
            binding.arabic.isChecked=true
        }
    }

    private fun checkLocation(){
        if(SharedPrefData.location== Utility.MAP){
            binding.map.isChecked=true
        }else{
            binding.gps.isChecked=true
        }
    }
}