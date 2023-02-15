package com.example.alarmapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BatteryLevelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        var counter = 0
        if(intent.action == Intent.ACTION_BATTERY_LOW){
            counter += 1
        }
        if(intent.action == Intent.ACTION_POWER_CONNECTED){
            counter += 1
        }
        if(intent.action == Intent.ACTION_BATTERY_OKAY){
            counter += 1
        }
        if(intent.action == Intent.ACTION_CALL){
            counter += 1
        }

        if (counter >= 2) {
            val stopServiceIntent = Intent(context, StartService::class.java).apply {
//                Toast.makeText(, "Service Stopped, Broadcast!", Toast.LENGTH_SHORT).show()
                Log.i("Service Status","Service Stopped due to broadcast message!")
                action = "com.your.package.action.STOP_SERVICE"
            }
            context.startService(stopServiceIntent)
        }
    }
}
