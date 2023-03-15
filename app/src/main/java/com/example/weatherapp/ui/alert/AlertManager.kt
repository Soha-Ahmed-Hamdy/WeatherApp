package com.example.weatherapp.ui.alert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import android.app.Application
import android.content.DialogInterface
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.model.LocalAlert
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.ApiState
import com.example.weatherapp.data.utils.SharedPrefData
import com.example.weatherapp.data.utils.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class AlertManager(private var context: Context) {

    @SuppressLint("ServiceCast", "SuspiciousIndentation")
    fun createAlert(alert: LocalAlert) {

        val utc = alert.time

        if ((utc!! *1000) > System.currentTimeMillis()) {

            if (SharedPrefData.notification == Utility.notification) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, BroadcastReceiverAlert::class.java)
                intent.putExtra("lat", alert.lattuide)
                intent.putExtra("lon", alert.longtuide)
                intent.putExtra("location", alert.zoneName)
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        alert.id.toInt(),
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                else {
                    PendingIntent.getBroadcast(
                        context,
                        alert.id.toInt(),
                        intent,
                        0
                    )
                }
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    utc!! * 1000L,
                    pendingIntent
                )

            }
            else if (SharedPrefData.notification == Utility.alert) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, BroadcastReceiverAlert::class.java)
                intent.putExtra("lat", alert.lattuide)
                intent.putExtra("lon", alert.longtuide)
                intent.putExtra("location", alert.zoneName)

                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        alert.id.toInt(),
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                else {
                    PendingIntent.getBroadcast(
                        context,
                        alert.id.toInt(),
                        intent,
                        0
                    )
                }
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    utc * 1000L,
                    pendingIntent
                )

            }
        }


    }

}

class BroadcastReceiverAlert : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TAG", "onReceive")
        val lon = intent?.getDoubleExtra("lon",0.0)
        val lat = intent?.getDoubleExtra("lat",0.0)
        val location = intent?.getStringExtra("location")

        val ApiStates = MutableStateFlow<ApiState>(ApiState.Loading)

        val myCoroutineScope = CoroutineScope(Dispatchers.IO)

        if (SharedPrefData.notification == Utility.notification) {
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "channelId",
                    "ChannelName",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }



            myCoroutineScope.launch {


                val data = Repository.getRepositoryInstance(context).getFavDetails(lat = lat!!.toDouble(),
                    long = lon!!.toDouble())

                data.catch {
                    ApiStates.value =ApiState.Failure(Throwable("No Internet"))
                }
                    .collectLatest{
                        ApiStates.value = ApiState.Success(it)
                    }


                ApiStates.collectLatest {

                    when (it) {
                        is ApiState.Success -> {
                            var event = context.getString(R.string.no_alerts)
                            if (it.weatherRoot?.alerts?.get(0) != null) {
                                event = context.getString(R.string.warning) + it.weatherRoot.alerts.get(0).event
                            }


                            val removeNotificationIntent = Intent(context, RemoveNotificationReceiver::class.java).apply {
                                putExtra("idNotification", 0)
                            }


                            val removeNotificationPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                PendingIntent.getBroadcast(
                                    context,
                                    0,
                                    removeNotificationIntent,
                                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                                )
                            }
                            else {
                                PendingIntent.getBroadcast(
                                    context,
                                    0,
                                    removeNotificationIntent,
                                    0
                                )
                            }

                            val intent = Intent(context, MainActivity::class.java)
                            val pendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            val builder = NotificationCompat.Builder(context, "channelId")
                                .setSmallIcon(R.drawable.dialog_img)
                                .setContentTitle("Alert")
                                .setContentText("$event ${context.getString(R.string.at)} $location.")
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setContentIntent(pendingIntent)
                                .addAction(R.drawable.dialog_img, context.getString(R.string.remove), removeNotificationPendingIntent)


                            notificationManager.notify(0, builder.build())

                            myCoroutineScope.cancel()
                        }
                        is ApiState.Failure -> {

                            val event = context?.getString(R.string.no_internet)

                            val removeNotificationIntent = Intent(context, RemoveNotificationReceiver::class.java).apply {
                                putExtra("idNotification", 0)
                            }


                            val removeNotificationPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getBroadcast(
                                context,
                                0,
                                removeNotificationIntent,
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )
                            else PendingIntent.getBroadcast(
                                context,
                                0,
                                removeNotificationIntent,
                                0
                            )

                            val intent = Intent(context, MainActivity::class.java)
                            val pendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            val builder = NotificationCompat.Builder(context!!, "channelId")
                                .setSmallIcon(R.drawable.dialog_img)
                                .setContentTitle("Alert")
                                .setContentText(event)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setContentIntent(pendingIntent)
                                .addAction(R.drawable.dialog_img, context?.getString(R.string.remove), removeNotificationPendingIntent)


                            notificationManager.notify(0, builder.build())

                            myCoroutineScope.cancel()
                        }
                        else -> {}
                    }
                }
            }

        }
        else if (SharedPrefData.notification == Utility.alert) {

            var ringtone: Ringtone? = null

            myCoroutineScope.launch {

                val data = Repository.getRepositoryInstance(context!!).getFavDetails(lat = lat!!.toDouble(),
                    long = lon!!.toDouble())

                data.catch {
                    ApiStates.value = ApiState.Failure(Throwable("No Internet"))
                }
                    .collectLatest{
                        ApiStates.value = ApiState.Success(it)
                    }


                ApiStates.collectLatest {

                    when (it) {
                        is ApiState.Success -> {
                            var event = context?.getString(R.string.no_alerts)
                            if (it.weatherRoot?.alerts != null) {
                                event = context?.getString(R.string.warning) + it.weatherRoot.alerts.get(0).event
                            }

                            val inflater = LayoutInflater.from(context)
                            val alertLayout = inflater.inflate(R.layout.dialog_alarm, null)

                            val params = WindowManager.LayoutParams(
                                convertDPtoPX(300, context!!),
                                convertDPtoPX(400, context!!),
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                                PixelFormat.TRANSLUCENT
                            )
                            params.gravity = Gravity.TOP or Gravity.CENTER
                            params.y = convertDPtoPX(200, context!!)


                            val btnRemove = alertLayout.findViewById<Button>(R.id.btn_cancel)
                            btnRemove.setOnClickListener {

                                ringtone?.stop()

                                val windowManager =
                                    context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                windowManager.removeViewImmediate(alertLayout)
                            }

                            val eventTextView = alertLayout.findViewById<TextView>(R.id.tv_event_name)
                            eventTextView.text = "$event ${context?.getString(R.string.at)} $location."

                            val windowManager =
                                context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                            withContext(Dispatchers.Main) {
                                windowManager.addView(alertLayout, params)
                            }


                            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            ringtone = RingtoneManager.getRingtone(context, alarmUri)
                            ringtone?.play()

                            myCoroutineScope.cancel()

                        }
                        is ApiState.Failure -> {
                            val event = context.getString(R.string.no_internet)

                            val inflater = LayoutInflater.from(context)
                            val alarmLayout = inflater.inflate(R.layout.dialog_alarm, null)

                            val params = WindowManager.LayoutParams(

                                convertDPtoPX(300, context),
                                convertDPtoPX(400, context),
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                                PixelFormat.TRANSLUCENT
                            )
                            params.gravity = Gravity.TOP or Gravity.CENTER
                            params.y = convertDPtoPX(200, context)

                            val btnRemove = alarmLayout.findViewById<Button>(R.id.btn_cancel)
                            btnRemove.setOnClickListener {

                                ringtone?.stop()

                                val windowManager =
                                    context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                windowManager.removeViewImmediate(alarmLayout)
                            }

                            val eventTextView = alarmLayout.findViewById<TextView>(R.id.tv_event_name)
                            eventTextView.text = event


                            val windowManager =
                                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                            withContext(Dispatchers.Main) {
                                windowManager.addView(alarmLayout, params)
                            }


                            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            ringtone = RingtoneManager.getRingtone(context, alarmUri)
                            ringtone?.play()

                            myCoroutineScope.cancel()
                        }
                        else -> {}
                    }
                }
            }
        }



    }

    private fun convertDPtoPX(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

class RemoveNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(intent.getIntExtra("idNotification", 0))

    }
}
