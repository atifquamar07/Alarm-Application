package com.example.alarmapplication

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.SimpleDateFormat
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.util.*

class StartService : Service() {

    private var alreadyPlayingRingtone: Boolean = false
    private var ringtone: Ringtone? = null
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var hourPlayed: Int = 0
    private var minutePlayed: Int = 0
    private var timeString: String = ""

    private val powerConnectionReceiver = PowerConnectionReceiver()
    private val batteryLevelReceiver = BatteryLevelReceiver()

    companion object {
        const val ACTION_STOP_SERVICE = "com.your.package.action.STOP_SERVICE"
    }

    @SuppressLint("ServiceCast")
    private fun checkTimeAndStartRingtone(inputHour: Int, inputMinutes: Int) {
        val calendar = Calendar.getInstance()
        val currHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currMinute = calendar.get(Calendar.MINUTE)
        val currTimeString = String.format("%02d:%02d ", currHour, currMinute)
        Log.i("Curr Time", currTimeString)

        if(alreadyPlayingRingtone && currHour == hourPlayed && currMinute == minutePlayed){
            return
        }

        val handler = Handler(Looper.getMainLooper())

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(powerConnectionReceiver, filter)

        val filterBattery = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        registerReceiver(batteryLevelReceiver, filterBattery)


        if (currHour == inputHour && currMinute == inputMinutes) {
            alreadyPlayingRingtone = true
            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
            val tst = String.format("Alarm started at %02d:%02d", inputHour, inputMinutes)
            Toast.makeText(applicationContext, tst, Toast.LENGTH_SHORT).show()
            Log.i("Alarm", tst)
            ringtone?.play()
            hourPlayed = currHour
            minutePlayed = currMinute
            handler.postDelayed({
                ringtone?.stop()
                Toast.makeText(applicationContext, "Alarm Stopped after 10 secs!", Toast.LENGTH_SHORT).show()
                Log.i("Alarm", "Alarm Stopped after 10 secs!")
            }, 10000) // stop after 10 seconds
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val inputHour1 = intent?.getIntExtra("hour1", 0)
        val inputMinutes1 = intent?.getIntExtra("minute1", 0)
        val inputHour2 = intent?.getIntExtra("hour2", 0)
        val inputMinutes2 = intent?.getIntExtra("minute2", 0)
        timeString = String.format("Time received in Service are %02d:%02d and %02d:%02d", inputHour1, inputMinutes1, inputHour2, inputMinutes2)
        Log.i("Time received in Service", timeString)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (inputHour1 != null && inputMinutes1 != null) {
                    checkTimeAndStartRingtone(inputHour1, inputMinutes1)
                }
                if (inputHour2 != null && inputMinutes2 != null) {
                    checkTimeAndStartRingtone(inputHour2, inputMinutes2)
                }
                handler.postDelayed(this, 10000) // 10 seconds
            }
        }
        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, StartService::class.java)
        Toast.makeText(applicationContext, "Service Destroyed!", Toast.LENGTH_SHORT).show()
        Log.i("ServiceUpdate", "Service Destroyed!")
        handler.removeCallbacks(runnable)
        unregisterReceiver(batteryLevelReceiver)
        unregisterReceiver(powerConnectionReceiver)
        ringtone?.stop()
        stopSelf()
        stopService(intent)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}


