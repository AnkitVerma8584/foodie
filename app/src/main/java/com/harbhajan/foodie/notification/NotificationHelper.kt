package com.harbhajan.foodie.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.CHANNEL_ONE_DESCRIPTION
import com.harbhajan.foodie.common.CHANNEL_ONE_ID
import com.harbhajan.foodie.common.CHANNEL_ONE_NAME


@TargetApi(Build.VERSION_CODES.O)
class NotificationHelper(
    context: Context
) : ContextWrapper(context) {


    private var notificationManager: NotificationManager? = null

    fun createNewOrderChannel() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val notificationChannel = NotificationChannel(
            CHANNEL_ONE_ID,
            CHANNEL_ONE_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.description = CHANNEL_ONE_DESCRIPTION
        notificationChannel.setShowBadge(true)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        notificationChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            attributes
        )
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        getManager().createNotificationChannel(notificationChannel)
    }

    fun getNewOrderNotification(
        title: String?,
        body: String?,
        myIntent: PendingIntent?
    ): Notification.Builder {
        return Notification.Builder(applicationContext, CHANNEL_ONE_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(myIntent)
            .setStyle(Notification.BigTextStyle().bigText(body))
            .setSmallIcon(R.drawable.dmlogo)
            .setAutoCancel(true)
    }

    fun notify(id: Int, notification: Notification.Builder) {
        getManager().notify(id, notification.build())
    }

    private fun getManager(): NotificationManager {
        if (notificationManager == null) {
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager as NotificationManager
    }
}