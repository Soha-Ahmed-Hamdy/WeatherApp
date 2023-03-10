package com.example.weatherapp.ui.alert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import com.bumptech.glide.Priority
import com.example.weatherapp.AlertBroadCastReceiver
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.model.AlertState
import com.example.weatherapp.model.LocalAlert
import com.example.weatherapp.model.repository.Repository
import com.example.weatherapp.ui.alert.alertAdapter.AlertAdapter
import com.example.weatherapp.ui.alert.alertViewModel.AlertViewModel
import com.example.weatherapp.ui.alert.alertViewModel.FactoryAlert
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

        val repository = Repository(requireContext())

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
                        alertAdapter = AlertAdapter(it.alertsData, listener)
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

    private fun displayNotification(context: Context){

        val channelId = "i.apps.notifications"
        var builder: Notification.Builder
        val description = "Test notification"

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentView = RemoteViews(context.packageName, R.layout.fragment_alert)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            val intent = Intent(context , AlertFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


            builder = Notification.Builder(context, channelId)
                .setContent(contentView)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setSmallIcon(R.drawable.dialog_img)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
                .setContentIntent(pendingIntent)
                .setStyle(Notification.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line..."))
                .setAutoCancel(true)

        } else {
            val intent = Intent(context, AlertFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


            builder = Notification.Builder(context)
                .setContent(contentView)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setSmallIcon(R.drawable.dialog_img)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        notificationManager.notify(1234, builder.build())
    }


}