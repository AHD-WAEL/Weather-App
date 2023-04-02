package eg.gov.iti.jets.weather.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import eg.gov.iti.jets.weather.Constants
import eg.gov.iti.jets.weather.R
import eg.gov.iti.jets.weather.db.ConcreteLocalSource
import eg.gov.iti.jets.weather.model.Repository
import eg.gov.iti.jets.weather.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val lat = intent.extras?.getString("lat")
        val lon = intent.extras?.getString("lon")
        var channelName = ""
        var channelDescription = ""
        var geoCoder = Geocoder(context)

        println("$lat ++++++++++++++++++++++++++++++++++++ $lon")

        val location = geoCoder.getFromLocation(lat!!.toDouble(), lon!!.toDouble(), 1) as MutableList<Address>
        val loc = location[0].adminArea.toString() + "/" + location[0].countryName.toString()

        runBlocking(Dispatchers.IO){
            Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource(context)).getLocation(lat.toString(), lon.toString(),"en").collect{
                if(it.alerts != null)
                {
                    channelName = "There is ${it.alerts[0].event} in $loc"
                    channelDescription = it.alerts[0].description
                }
                else
                {
                    channelName = "No Alerts"
                    channelDescription = "No Alerts Received in $loc"
                }
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channelNotification = NotificationChannel("notification1", channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channelNotification.description = channelDescription
            notificationManager.createNotificationChannel(channelNotification)
        }

        val notificationBuild = NotificationCompat.Builder(context, "notification1")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(channelName)
            .setContentText(channelDescription)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        notificationManager.notify(0, notificationBuild.build())

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
        var alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if(alarm == null) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val settings = context.getSharedPreferences(Constants.settingPreferences,Context.MODE_PRIVATE)
        if(settings.getString("notification","none") == "alert"){
            val ringtone = RingtoneManager.getRingtone(context, alarm)
            ringtone.play()
        }
        abortBroadcast()
    }
}