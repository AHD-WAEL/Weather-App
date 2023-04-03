package eg.gov.iti.jets.weather.alert.view

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Binder
import android.os.IBinder

class MyService : Service() {

    private val binder: IBinder = MyBinder()
    lateinit var ringtone: Ringtone

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MyBinder: Binder(){
        fun getService(): MyService{
            return this@MyService
        }
    }

    override fun onCreate() {
        super.onCreate()
        var alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if(alarm == null) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        ringtone = RingtoneManager.getRingtone(applicationContext, alarm)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("++++++++++++++++++++++++")
        ringtone.play()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop()
    }
}