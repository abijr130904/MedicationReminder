package com.example.medicationreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        val medicationName = intent.getStringExtra("medication_name") ?: "Unknown Medication"
        val doseAmount = intent.getStringExtra("dose_amount") ?: "Unknown Dose"

        // Menampilkan pesan toast sebagai pengingat
        Toast.makeText(context, "Time to take $medicationName. Dose: $doseAmount", Toast.LENGTH_LONG).show()

        // Memutar suara alarm
        mediaPlayer = MediaPlayer.create(context,R.raw.alarm_sound)
        mediaPlayer?.start()

        // Agar suara berhenti setelah selesai
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    // Pastikan untuk membersihkan MediaPlayer jika dibutuhkan
    fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
