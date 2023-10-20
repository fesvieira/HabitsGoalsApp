package com.fesvieira.habitsgoals.helpers

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat.getActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fesvieira.habitsgoals.MainActivity
import com.fesvieira.habitsgoals.R

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val NOTIFICATION_ID = "reminder_for_habit_"
        const val NOTIFICATION_CHANNEL = "General"
        const val NOTIFICATION_NAME = "Habit Reminder"
        const val HABIT_NAME = "habit_name"
        const val HABIT_ID = "habit_id"
        const val IS_FIRST = "isFirst"
    }

    override fun doWork(): Result {
        val habitName = inputData.getString(HABIT_NAME) ?: return Result.failure()
        val habitId = inputData.getInt(HABIT_ID, 0)
        val isFirstNotification = inputData.getBoolean(IS_FIRST, false)

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, "$NOTIFICATION_ID$habitId")

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent =
            getActivity(applicationContext, 0, intent, 0, false)

        val sortMessage = applicationContext.getString(
            if (isFirstNotification) R.string.this_is_an_example_of_reminder_for
            else
                when ((0..1).random()) {
                    0 -> R.string.dont_forget
                    else -> R.string.keep_going
                },
            habitName
        )

        val notification = NotificationCompat
            .Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_checklist)
            .setContentTitle(NOTIFICATION_NAME)
            .setContentText(sortMessage)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = IMPORTANCE_HIGH
        notification.setChannelId(NOTIFICATION_CHANNEL)

        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes
            .Builder()
            .setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION)
            .build()

        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, IMPORTANCE_HIGH)

        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(habitId, notification.build())

        return Result.success()
    }
}