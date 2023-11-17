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
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.HABIT_ID
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.HABIT_NAME
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.NOTIFICATION_CHANNEL
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.NOTIFICATION_ID
import java.time.LocalDateTime


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val habitName = intent.getStringExtra(HABIT_NAME)
        val habitId = intent.getStringExtra(HABIT_ID) ?: "0"

        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mainActivityIntent.putExtra(NOTIFICATION_ID, habitName)

        val pendingIntent = PendingIntentCompat
            .getActivity(
                context,
                habitId.toInt(),
                mainActivityIntent,
                0,
                false
            )

        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Habit & Goals Reminder!!")
            .setContentText("Reminder for habit: $habitName")
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
        notificationManager.notify(habitId.toInt(), notification.build())

        val alarmScheduler = AndroidAlarmScheduler(context)
        if (habitName != null) {
            alarmScheduler.schedule(
                item = AlarmItem(
                    habitId.toInt(),
                    habitName,
                    time = LocalDateTime.now().plusHours(24)
                )
            )
        }
    }
}