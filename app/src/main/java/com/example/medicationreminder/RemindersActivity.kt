package com.example.medicationreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        val remindersListView = findViewById<ListView>(R.id.remindersListView)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)
        val allReminders = sharedPreferences.all

        val reminderList = allReminders.map {
            val key = it.key
            val dose = it.value.toString()
            "Reminder: $key - Dose: $dose"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)
        remindersListView.adapter = adapter
    }
}
