package no.hiof.discgolfapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {


    private fun displayNotification(notificationTitle: String, notificationMessage: String ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setSmallIcon(R.mipmap.frisbee_golf_icon_round)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FCM message", "From: ${remoteMessage.from}")

        Log.d("FCM RawData", "${remoteMessage.rawData}")
        Log.d("FCM Data", "${remoteMessage.data}")

        displayNotification("", "Hei, lenge siden vi har sett deg! Spill en runde med frisbeegolf i dag!")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // TODO: Problem, the data is null, and needs to be fixed, but I don't understand why, I can`t figure it out
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
            try {
                displayNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
            } catch (_: java.lang.NullPointerException) {}


        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }

    }



}