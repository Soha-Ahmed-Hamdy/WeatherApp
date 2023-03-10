package com.example.weatherapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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

        displayNotification(context)
    }
}
private fun displayNotification(context: Context){

    val channelId = "i.apps.notifications"
    var builder: Notification.Builder
    val description = "Test notification"

    val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val contentView = RemoteViews(context.packageName, R.layout.fragment_alert)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        val intent = Intent(context , AlertBroadCastReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        builder = Notification.Builder(context, channelId)
            .setContent(contentView)
            .setSmallIcon(R.drawable.dialog_img)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    } else {
        val intent = Intent(context, AlertBroadCastReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        builder = Notification.Builder(context)
            .setContent(contentView)
            .setSmallIcon(R.drawable.dialog_img)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.dialog_img))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
    notificationManager.notify(1234, builder.build())
}