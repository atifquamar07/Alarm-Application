package com.example.alarmapplication

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import com.example.alarmapplication.fragments.TimePickerFragment


class MainActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener {

    private lateinit var btStart: Button
    private lateinit var btStop: Button
    private lateinit var addAlarm: ImageButton
    private var hour: Int = 0
    private var min: Int = 0
    private var timeString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addAlarm = findViewById(R.id.add_icon)
        addAlarm.setOnClickListener{
            showTimePicker()
        }

        val intent = Intent(this, StartService::class.java)

        btStart = findViewById(R.id.btStart)
        btStart.setOnClickListener{
            intent.putExtra("hour", hour)
            intent.putExtra("minute", min)
            Toast.makeText(applicationContext, "Service Started!", Toast.LENGTH_SHORT).show()
            Log.i("Service Status","Service Started!")
            startService(intent)
        }

        btStop = findViewById(R.id.btStop)
        btStop.setOnClickListener{
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
        hour = hourOfDay
        min = minute
        timeString = String.format("%02d:%02d", hourOfDay, minute)
        Toast.makeText(applicationContext, timeString, Toast.LENGTH_SHORT).show()
        Log.i("Time selected: ",timeString)
    }
}