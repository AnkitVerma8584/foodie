package com.harbhajan.foodie.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harbhajan.foodie.R
import com.harbhajan.foodie.common.CHANNEL_ONE_ID
import com.harbhajan.foodie.common.printLog
import com.harbhajan.foodie.ui.activities.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Keep
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val data: Map<String, String> = p0.data
        val title = data["title"]
        val body = data["message"]

        data.printLog()

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val id = System.currentTimeMillis().toInt()
        @SuppressLint("UnspecifiedImmutableFlag") val intent = PendingIntent.getActivity(
            applicationContext,
            id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.createNewOrderChannel()
            val notificationBuilder: Notification.Builder =
                notificationHelper.getNewOrderNotification(title, body, intent)
            notificationBuilder.setShowWhen(true)
            notificationHelper.notify(id, notificationBuilder)
        } else {
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                    .setSmallIcon(R.drawable.dmlogo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setShowWhen(true)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setContentIntent(intent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

            NotificationManagerCompat.from(applicationContext).notify(id, builder.build())
        }
    }
}