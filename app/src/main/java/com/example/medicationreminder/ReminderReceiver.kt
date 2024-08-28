package com.example.medicationreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicationName = intent.getStringExtra("medication_name")
        val doseAmount = intent.getStringExtra("dose_amount")

        // Tampilkan Toast
        Toast.makeText(context, "Time to take $medicationName. Dose: $doseAmount", Toast.LENGTH_LONG).show()

        // Memutar suara alarm
        playAlarmSound(context)

        // Buat notifikasi
        createNotification(context, medicationName, doseAmount)
    }

    private fun playAlarmSound(context: Context) {
        // Pastikan file audio ada di folder res/raw dan ganti R.raw.alarm_sound dengan nama file audio Anda
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun createNotification(context: Context, medicationName: String?, doseAmount: String?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat intent untuk membuka aplikasi saat notifikasi diklik
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Buat channel notifikasi untuk Android Oreo dan versi lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "medication_reminder_channel"
            val channelName = "Medication Reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Buat notifikasi
        val notificationBuilder = NotificationCompat.Builder(context, "medication_reminder_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti dengan ikon notifikasi Anda
            .setContentTitle("Medication Reminder")
            .setContentText("Time to take $medicationName. Dose: $doseAmount")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Tampilkan notifikasi
        notificationManager.notify(0, notificationBuilder.build())
    }
}