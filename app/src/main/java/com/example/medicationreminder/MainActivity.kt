package com.example.medicationreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var remindersListView: ListView
    private lateinit var medicationName: EditText
    private lateinit var doseAmount: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var remindersList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)
        remindersListView = findViewById(R.id.remindersListView)
        medicationName = findViewById(R.id.medicationName)
        doseAmount = findViewById(R.id.doseAmount)
        timePicker = findViewById(R.id.timePicker)
        remindersList = sharedPreferences.all.keys.toMutableList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, remindersList)
        remindersListView.adapter = adapter

        findViewById<Button>(R.id.setReminderButton).setOnClickListener {
            addReminder()
        }

        remindersListView.setOnItemClickListener { _, _, position, _ ->
            removeReminder(position)
        }
    }

    private fun addReminder() {
        val name = medicationName.text.toString()
        val dose = doseAmount.text.toString()
        val hour = timePicker.hour
        val minute = timePicker.minute

        if (name.isNotBlank() && dose.isNotBlank()) {
            val reminder = "$name - $dose at $hour:$minute"
            remindersList.add(reminder)

            val editor = sharedPreferences.edit()
            editor.putString(reminder, "$name-$dose-$hour-$minute")
            editor.apply()

            adapter.notifyDataSetChanged()

            setAlarm(name, dose, hour, minute)
            medicationName.text.clear()
            doseAmount.text.clear()
        }
    }

    private fun removeReminder(position: Int) {
        val reminder = remindersList[position]
        val editor = sharedPreferences.edit()
        editor.remove(reminder)
        editor.apply()

        remindersList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun setAlarm(name: String, dose: String, hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("medication_name", name)
            putExtra("dose_amount", dose)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
