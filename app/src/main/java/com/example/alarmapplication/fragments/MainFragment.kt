package com.example.alarmapplication.fragments

import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.alarmapplication.R
import com.example.alarmapplication.StartService


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), TimePickerFragmentClass.TimePickerListener {

    private lateinit var btStart: Button
    private lateinit var btStop: Button
    private lateinit var alarmListLayout: LinearLayout
    private lateinit var alarm1text: TextView
    private lateinit var alarm2text: TextView
    private lateinit var addAlarm: ImageButton
    private var hour1: Int = 0
    private var min1: Int = 0
    private var hour2: Int = 0
    private var min2: Int = 0
    private var isAlarm1Set: Boolean = false
    private var isAlarm2Set: Boolean = false
    private var timeString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // ...
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAlarm = view.findViewById(R.id.add_icon)
        addAlarm.setOnClickListener{
            checkIfFragmentAttached {
                showTimePicker()
            }
        }

        alarmListLayout = view.findViewById(R.id.layout_id)
        alarm1text = view.findViewById(R.id.id_alarm1)
        alarm2text = view.findViewById(R.id.id_alarm2)

        btStart = view.findViewById(R.id.btStart)

        btStart.setOnClickListener{
            checkIfFragmentAttached {
                val intent = Intent(requireActivity(), StartService::class.java)
                intent.putExtra("hour1", hour1)
                intent.putExtra("minute1", min1)
                intent.putExtra("hour2", hour2)
                intent.putExtra("minute2", min2)
                timeString = String.format("%02d:%02d and %02d:%02d", hour1, min1, hour2, min2)
                Toast.makeText(requireActivity(), "Service Started!", Toast.LENGTH_SHORT).show()
                Log.i("Time going out",timeString)
                Log.i("Service Status","Service Started!")
                if(isAlarm1Set || isAlarm2Set){
                    if(isAlarm1Set){
                        val alr1 = String.format("Alarm is set for %02d:%02d", hour1, min1)
                        alarm1text.text = alr1
                        alarm1text.visibility = View.VISIBLE
                    }
                    if(isAlarm2Set){
                        val alr1 = String.format("Alarm is set for %02d:%02d", hour2, min2)
                        alarm2text.text = alr1
                        alarm2text.visibility = View.VISIBLE
                    }
                    alarmListLayout.visibility = View.VISIBLE
                }
                activity?.startService(intent)
            }
        }

        val filter = IntentFilter(StartService.ACTION_STOP_SERVICE)
        checkIfFragmentAttached {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(receiver, filter)
        }

        btStop = view.findViewById(R.id.btStop)
        btStop.setOnClickListener{
            val intent = Intent()
            checkIfFragmentAttached {
                intent.setClass(requireActivity(), StartService::class.java)
                intent.action = StartService.ACTION_STOP_SERVICE
                checkIfFragmentAttached {
                    Toast.makeText(requireActivity(), "Service Stopped!", Toast.LENGTH_SHORT).show()
                }
                Log.i("Service Status","Service Stopped!")
                activity?.stopService(intent)
            }
        }

    }

    private fun showTimePicker() {
        val timePickerFragment = TimePickerFragmentClass()
        timePickerFragment.show(childFragmentManager, "timePicker")
    }
    override fun onTimeSet(hourOfDay: Int, minute: Int) {
        Log.d("MyFragment", "Time set: $hourOfDay:$minute")
        if(!isAlarm1Set){
            hour1 = hourOfDay
            min1 = minute
            isAlarm1Set = true
        }
        else {
            hour2 = hourOfDay
            min2 = minute
            isAlarm2Set = true
        }

        timeString = String.format("Alarm set for %02d:%02d", hourOfDay, minute)
        checkIfFragmentAttached {
            Toast.makeText(requireActivity(), timeString, Toast.LENGTH_SHORT).show()
        }
        Log.i("Time selected: ",timeString)
    }


    companion object {

        fun newInstance(): MainFragment = MainFragment()
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}