package com.fesvieira.habitsgoals.alarmmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.HABIT_ID
import com.fesvieira.habitsgoals.alarmmanager.AlarmDetails.Companion.HABIT_NAME
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun schedule(item: AlarmItem) {

        val notificationIntent = Intent(context, AlarmReceiver::class.java)
        notificationIntent.putExtra(HABIT_ID, item.habitId.toString())
        notificationIntent.putExtra(HABIT_NAME, item.habitName)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.habitId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var alarmDate = item.time.atZone(ZoneId.systemDefault())
        val timeToAlarm = Duration.between(LocalDateTime.now(), alarmDate).toMillis()
        if (timeToAlarm < 0) {
           alarmDate = alarmDate.plusDays(1)
        }

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