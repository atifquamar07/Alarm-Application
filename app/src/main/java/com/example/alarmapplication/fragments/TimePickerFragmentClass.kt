package com.example.alarmapplication.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimePickerFragmentClass.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimePickerFragmentClass : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    var onTimeSetListener: OnTimeSetListener? = null
    private var listener: TimePickerListener? = null


    interface OnTimeSetListener {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }
    interface TimePickerListener {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Get the current time as the default time for the TimePicker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new TimePickerDialog with the default time and return it
        return TimePickerDialog(requireActivity(), this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is TimePickerListener) {
            listener = parentFragment as TimePickerListener
        } else {
            throw RuntimeException("Parent fragment must implement TimePickerListener")
        }
    }
    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener?.onTimeSet(hourOfDay, minute)
    }


//    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//
//        // Call the onTimeSet() function of the OnTimeSetListener with the selected time
//        onTimeSetListener?.onTimeSet(hourOfDay, minute)
//    }
}