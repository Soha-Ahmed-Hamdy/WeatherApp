package com.example.weatherapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews

class AlertBroadCastReceiver : BroadcastReceiver() {

    private val description = "Test notification"

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("AlertBroadCastReceiver.onReceive() is not implemented")
        displayNotification(context)
    }
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