package com.example.alarmapplication

import com.example.alarmapplication.R
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapplication.fragments.MainFragment


class MainActivity : AppCompatActivity() {

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("in main", "main")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mFragment = MainFragment.newInstance()
        openFragment(mFragment)
    }


}