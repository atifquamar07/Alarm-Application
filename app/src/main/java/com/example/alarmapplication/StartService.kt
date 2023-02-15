package com.example.alarmapplication

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
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

    companion object {
        const val ACTION_STOP_SERVICE = "com.your.package.action.STOP_SERVICE"
    }

    @SuppressLint("ServiceCast")
    private fun checkTimeAndStartRingtone(inputHour: Int, inputMinutes: Int) {
        if(alreadyPlayingRingtone){
            return
        }
//        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//        ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)

        val calendar = Calendar.getInstance()
        val currHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currMinute = calendar.get(Calendar.MINUTE)
        val currTimeString = String.format("%02d:%02d ", currHour, currMinute)
        Log.i("Curr Time", currTimeString)

        val handler = Handler(Looper.getMainLooper())

        if (currHour == inputHour && currMinute == inputMinutes) {
            alreadyPlayingRingtone = true
            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
            Toast.makeText(applicationContext, "Alarm Started!", Toast.LENGTH_SHORT).show()
            Log.i("Alarm", "Alarm Started!")
            ringtone?.play()
            handler.postDelayed({
                ringtone?.stop()
                Toast.makeText(applicationContext, "Alarm Stopped after 10 secs!", Toast.LENGTH_SHORT).show()
                Log.i("Alarm", "Alarm Stopped after 10 secs!")
                stopSelf()
            }, 10000) // stop after 10 seconds
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val inputHour = intent?.getIntExtra("hour", 0)
        val inputMinutes = intent?.getIntExtra("minute", 0)
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (inputHour != null && inputMinutes != null) {
                    checkTimeAndStartRingtone(inputHour, inputMinutes)
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
        ringtone?.stop()
        stopSelf()
        stopService(intent)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}


