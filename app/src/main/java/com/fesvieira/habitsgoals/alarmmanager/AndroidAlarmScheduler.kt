package com.fesvieira.habitsgoals.alarmmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AndroidAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun schedule(item: AlarmItem) {

        val notificationIntent = Intent(context, AlarmReceiver::class.java)
        notificationIntent.putExtra("id", item.habitId.toString())
        notificationIntent.putExtra("habitName", item.habitName)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.habitId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmDate = item.time.atZone(ZoneId.systemDefault())
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmDate.toEpochSecond() * 1000,
            pendingIntent
        )
    }

    fun cancel(habitId: Int) {
        val intent = PendingIntent.getBroadcast(
            context,
            habitId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(intent)
    }
}