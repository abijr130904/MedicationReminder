package com.example.medicationreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewRemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reminders)

        // Temukan ListView
        val remindersListView = findViewById<ListView>(R.id.remindersListView)

        // Ambil SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)
        val allReminders = sharedPreferences.all

        // Buat daftar pengingat
        val reminderList = allReminders.map { entry ->
            val key = entry.key
            val dose = entry.value.toString()
            "Reminder: $key - Dose: $dose"
        }.toList() // Konversi ke List

        // Buat ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)

        // Set adapter ke ListView
        remindersListView.adapter = adapter
    }
}
