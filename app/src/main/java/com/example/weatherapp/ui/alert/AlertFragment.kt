package com.example.weatherapp.ui.alert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.data.utils.AlertState
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.ui.alert.alertAdapter.AlertAdapter
import com.example.weatherapp.ui.alert.alertViewModel.AlertViewModel
import com.example.weatherapp.ui.alert.alertViewModel.FactoryAlert
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var fact: FactoryAlert
    private lateinit var alertAdapter: AlertAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        checkOverlayPermission()

        val repository = Repository.getRepositoryInstance(requireActivity().application)

        fact = FactoryAlert(repository)
        alertViewModel =
            ViewModelProvider(requireActivity(), fact).get(AlertViewModel::class.java)

        lifecycleScope.launch {
            alertViewModel.alert.collectLatest {
                when (it) {

                    is AlertState.Loading -> {

                    }
                    is AlertState.Success -> {

                        val listener = { alert: LocalAlert ->
                            alertViewModel.deleteAlert(alert)
                        }
                        alertAdapter = AlertAdapter(requireContext(),it.alertsData, listener)
                        binding.alertRecycler.adapter = alertAdapter


                    }
                    is AlertState.Failure -> {
                        Toast.makeText(requireContext(), "Failure $it", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }

        binding.floatingAddAlert.setOnClickListener {

            Navigation.findNavController(root)
                .navigate(R.id.alertSpecificationFragment3)
        }


        return root
    }

    private fun checkOverlayPermission() {
        if (SharedPrefData.notification==Utility.alert){
            if (!Settings.canDrawOverlays(requireContext())) {
                val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                alertDialogBuilder.setTitle(getString(R.string.alerts))
                    .setMessage(getString(R.string.alerts))
                    .setPositiveButton(getString(R.string.alerts)) { dialog: DialogInterface, i: Int ->
                        var myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        startActivity(myIntent)
                        dialog.dismiss()
                    }.setNegativeButton(
                        getString(R.string.cancel)
                    ) { dialog: DialogInterface, i: Int ->
                        dialog.dismiss()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }.show()
            }
        }

    }
}