package eg.gov.iti.jets.weather.alert.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ServiceCaller : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, MyService::class.java)
        context.stopService(service)
    }
}