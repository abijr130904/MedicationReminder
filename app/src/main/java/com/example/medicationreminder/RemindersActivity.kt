package com.example.medicationreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RemindersActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var remindersListView: ListView
    private lateinit var medicationName: EditText
    private lateinit var doseAmount: EditText
    private lateinit var deleteAllButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders) // Pastikan ini sesuai dengan layout yang benar

        sharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)
        remindersListView = findViewById(R.id.remindersListView)
        medicationName = findViewById(R.id.medicationName)
        doseAmount = findViewById(R.id.doseAmount)
        deleteAllButton = findViewById(R.id.deleteRemindersButton)

        findViewById<Button>(R.id.setReminderButton).setOnClickListener {
            addReminder()
        }

        deleteAllButton.setOnClickListener {
            deleteAllReminders()
        }

        loadReminders()

        // Handle item clicks to remove reminders
        remindersListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            removeReminder(position)
        }
    }

    private fun addReminder() {
        val name = medicationName.text.toString()
        val dose = doseAmount.text.toString()

        if (name.isNotBlank() && dose.isNotBlank()) {
            val editor = sharedPreferences.edit()
            editor.putString(name, dose)
            editor.apply()

            medicationName.text.clear()
            doseAmount.text.clear()

            loadReminders()
        }
    }

    private fun loadReminders() {
        val allReminders = sharedPreferences.all

        val reminderList = allReminders.map { (key, value) ->
            "Reminder: $key - Dose: ${value.toString()}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)
        remindersListView.adapter = adapter
    }

    private fun removeReminder(position: Int) {
        val reminderText = (remindersListView.adapter as ArrayAdapter<*>).getItem(position) as String
        val reminderName = reminderText.split(" - ")[0].replace("Reminder: ", "")

        val editor = sharedPreferences.edit()
        editor.remove(reminderName)
        editor.apply()

        loadReminders()
    }

    private fun deleteAllReminders() {
        val editor = sharedPreferences.edit()
        editor.clear() // Remove all entries
        editor.apply()

        loadReminders()
    }
}
