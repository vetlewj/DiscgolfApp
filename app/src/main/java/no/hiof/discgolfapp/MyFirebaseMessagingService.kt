package no.hiof.discgolfapp

import android.annotation.SuppressLint
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

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "no.hiof.discgolfapp"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    fun getRemoteView(notificationMessage: String): RemoteViews {
        val remoteView = RemoteViews(CHANNEL_NAME, R.layout.notification)

        remoteView.setTextViewText(R.id.notificationTextView, notificationMessage)
        remoteView.setImageViewResource(R.id.logo_for_app, R.mipmap.frisbee_golf_icon_round)
        return remoteView
    }

    fun generateNotification(notificationMessage: String ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.frisbee_golf_icon_round)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(notificationMessage))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(1, builder.build())


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FCM message", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // TODO: Problem, the data is null, and needs to be fixed, but I don't understand why, I can`t figure it out
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
            remoteMessage.notification?.body?.let { generateNotification(it) }

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }

    }



}