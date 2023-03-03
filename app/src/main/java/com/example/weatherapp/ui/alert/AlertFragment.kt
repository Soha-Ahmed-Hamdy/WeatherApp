package com.example.weatherapp.ui.alert

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.ui.alert.alertViewModel.AlertViewModel
import com.example.weatherapp.ui.alert.alertViewModel.FactoryAlert
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel

class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    lateinit var alertViewModel: AlertViewModel
    lateinit var fact: FactoryAlert


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fact = FactoryAlert(requireContext())
        alertViewModel =
            ViewModelProvider(requireActivity(), fact).get(AlertViewModel::class.java)

        binding.floatingAddAlert.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.alertSpecificationFragment)
        }

        return root
    }

    private fun displayNotification(context: Context){

        val channelId = "i.apps.notifications"
        var builder: Notification.Builder
        val description = "Test notification"

        var notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentView = RemoteViews(context.packageName, R.layout.fragment_alert)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.dialog_img)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
            // .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(context)
                .setContent(contentView)
                .setSmallIcon(R.drawable.dialog_img)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
            //.setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
    }


}