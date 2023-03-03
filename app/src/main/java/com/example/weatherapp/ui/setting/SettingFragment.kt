package com.example.weatherapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.ui.home.Utility


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        changeLanguage()


        return root
    }
    private fun refreshFragment(){
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }
    private fun changeLanguage(){
        binding.radioGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.english.id -> {

                    Utility.saveLanguageToSharedPref(requireContext()
                        , Utility.Language_Key, Utility.Language_EN_Value)
                    LocaleManager.setLocale(requireContext())
                    refreshFragment()

                }
                checkedButtonId == binding.arabic.id -> {

                Utility.saveLanguageToSharedPref(requireContext()
                    , Utility.Language_Key
                    , Utility.Language_AR_Value)
                LocaleManager.setLocale(requireContext())
                refreshFragment()
                    refreshFragment()

            }
            }

        }
    }
}