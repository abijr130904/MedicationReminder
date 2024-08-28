package com.example.medicationreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var timeDisplay: TextView
    private var selectedHour = 9
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val medicationName = findViewById<EditText>(R.id.medicationName)
        val doseAmount = findViewById<EditText>(R.id.doseAmount)
        timeDisplay = findViewById(R.id.timeDisplay)
        val setTimeButton = findViewById<Button>(R.id.setTimeButton)
        val setReminderButton = findViewById<Button>(R.id.setReminderButton)
        val viewRemindersButton = findViewById<Button>(R.id.viewRemindersButton)

        setTimeButton.setOnClickListener {
            showTimePickerDialog()
        }

        setReminderButton.setOnClickListener {
            val name = medicationName.text.toString()
            val dose = doseAmount.text.toString()

            if (name.isNotEmpty() && dose.isNotEmpty()) {
                setReminder(name, dose)
                Toast.makeText(this, "Reminder set for $name at $selectedHour:$selectedMinute", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter medication name and dose", Toast.LENGTH_SHORT).show()
            }
        }

        viewRemindersButton.setOnClickListener {
            val intent = Intent(this, ViewRemindersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            selectedHour = hourOfDay
            selectedMinute = minute
            timeDisplay.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, selectedHour, selectedMinute, true)
        timePickerDialog.show()
    }

    private fun setReminder(name: String, dose: String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("medication_name", name)
            putExtra("dose_amount", dose)
            putExtra("hour", selectedHour)
            putExtra("minute", selectedMinute)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        saveReminder(name, dose, selectedHour, selectedMinute)
    }

    private fun saveReminder(name: String, dose: String, hour: Int, minute: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val key = "${name}_${hour}_${minute}"
        editor.putString(key, dose)
        editor.putInt("${name}_${hour}_${minute}_hour", hour)
        editor.putInt("${name}_${hour}_${minute}_minute", minute)
        editor.apply()
    }
}
