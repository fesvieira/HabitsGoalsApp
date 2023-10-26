package com.fesvieira.habitsgoals.helpers.alarmmanager

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}