package com.example.medicationreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewRemindersActivity : AppCompatActivity() {

    private lateinit var remindersListView: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ReminderAdapter
    private lateinit var remindersList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reminders)

        remindersListView = findViewById(R.id.remindersListView)
        sharedPreferences = getSharedPreferences("MedicationReminders", Context.MODE_PRIVATE)

        remindersList = sharedPreferences.all.keys.toMutableList()
        adapter = ReminderAdapter(this, remindersList)
        remindersListView.adapter = adapter

        // Optionally, handle item click events here
        remindersListView.setOnItemClickListener { _, _, position, _ ->
            // Implement any action on item click if needed
        }
    }

    private fun deleteReminder(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    private inner class ReminderAdapter(context: Context, reminders: List<String>) :
        ArrayAdapter<String>(context, R.layout.list_item_reminder, reminders) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_reminder, parent, false)
            val reminderTextView: TextView = view.findViewById(R.id.reminderTextView)
            val deleteButton: Button = view.findViewById(R.id.deleteButton)

            val reminder = getItem(position)
            reminderTextView.text = reminder

            deleteButton.setOnClickListener {
                val reminderKey = reminder?.split(" - ")?.first()?.replace("Reminder: ", "")
                if (reminderKey != null) {
                    deleteReminder(reminderKey)
                    remindersList.removeAt(position)
                    notifyDataSetChanged()
                }
            }

            return view
        }
    }
}
