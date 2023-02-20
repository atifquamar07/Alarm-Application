package com.example.alarmapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapplication.fragments.TimePickerFragment


class MainActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener {

    private lateinit var btStart: Button
    private lateinit var btStop: Button
    private lateinit var addAlarm: ImageButton
    private var hour1: Int = 0
    private var min1: Int = 0
    private var hour2: Int = 0
    private var min2: Int = 0
    private var timeString: String = ""

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // ...
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addAlarm = findViewById(R.id.add_icon)
        addAlarm.setOnClickListener{
            showTimePicker()
        }

        btStart = findViewById(R.id.btStart)
        btStart.setOnClickListener{
            val intent = Intent(this, StartService::class.java)
            intent.putExtra("hour1", hour1)
            intent.putExtra("minute1", min1)
            intent.putExtra("hour2", hour2)
            intent.putExtra("minute2", min2)
            Toast.makeText(applicationContext, "Service Started!", Toast.LENGTH_SHORT).show()
            Log.i("Service Status","Service Started!")
            startService(intent)
        }

        val filter = IntentFilter(StartService.ACTION_STOP_SERVICE)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        btStop = findViewById(R.id.btStop)
        btStop.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, StartService::class.java)
            intent.action = StartService.ACTION_STOP_SERVICE
            Toast.makeText(applicationContext, "Service Stopped!", Toast.LENGTH_SHORT).show()
            Log.i("Service Status","Service Stopped!")
            stopService(intent)
        }

    }
    private fun showTimePicker() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.onTimeSetListener = this // Set the onTimeSetListener to the main activity
        timePickerFragment.show(supportFragmentManager, "timePicker")
    }

    override fun onTimeSet(hourOfDay: Int, minute: Int) {
        if(hour1 == 0){
            hour1 = hourOfDay
            min1 = minute
        }
        else {
            hour2 = hourOfDay
            min2 = minute
        }

        timeString = String.format("Alarm set for %02d:%02d", hourOfDay, minute)
        Toast.makeText(applicationContext, timeString, Toast.LENGTH_SHORT).show()
        Log.i("Time selected: ",timeString)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}