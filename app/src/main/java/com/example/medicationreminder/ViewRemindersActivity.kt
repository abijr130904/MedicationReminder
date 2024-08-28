package com.example.medicationreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewRemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reminders)

        val remindersTextView = findViewById<TextView>(R.id.remindersTextView)

        // Mendapatkan SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)

        // Mengambil semua pengingat yang tersimpan
        val allReminders = sharedPreferences.all
        val remindersStringBuilder = StringBuilder()

        for ((key, value) in allReminders.entries) {
            remindersStringBuilder.append("$key: $value\n")
        }

        // Menampilkan pengingat di TextView
        remindersTextView.text = remindersStringBuilder.toString()
    }
}
