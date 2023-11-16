package com.fesvieira.habitsgoals.alarmmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import com.fesvieira.habitsgoals.MainActivity
import com.fesvieira.habitsgoals.R


class AlarmReceiver: BroadcastReceiver(){
    companion object{
        const val NOTIFICATION_ID = "habit_reminder"
        const val NOTIFICATION_CHANNEL = "Habit Reminder"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val newIntent = Intent(context, MainActivity::class.java)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        newIntent.putExtra(NOTIFICATION_ID, intent.getStringExtra("id"))
        val pendingIntent = PendingIntentCompat.getActivity(context, 0, newIntent, 0, false)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Habit & Goals Reminder!!")
            .setContentText("Reminder for habit: ${intent.getStringExtra("habitName")}")
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationManager.IMPORTANCE_HIGH
        notification.setChannelId(NOTIFICATION_CHANNEL)

        val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes
            .Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL,
            NOTIFICATION_CHANNEL,
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(intent.getStringExtra("id")?.toIntOrNull() ?: 0, notification.build())
    }
}