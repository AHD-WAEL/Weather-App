package eg.gov.iti.jets.weather.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import eg.gov.iti.jets.weather.R
import okhttp3.internal.notify

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val lat = intent.extras?.getString("lat")
        val lon = intent.extras?.getString("lon")
        println(lat+"+++++++++++++++++"+lon)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channelName = "Channel"
            val channelDescription = "Description"
            val channelNotification = NotificationChannel("notification1", channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channelNotification.description = channelDescription
            notificationManager.createNotificationChannel(channelNotification)
        }

        val notificationBuild = NotificationCompat.Builder(context, "notification1")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("notification1")
            .setContentText("notification1 txt")
            .setPriority(NotificationCompat.PRIORITY_MAX)
        notificationManager.notify(0, notificationBuild.build())

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
        var alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if(alarm == null)
        {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(context, alarm)
        ringtone.play()
    }
}