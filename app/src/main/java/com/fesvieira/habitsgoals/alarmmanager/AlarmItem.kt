package com.fesvieira.habitsgoals.alarmmanager

import java.time.LocalDateTime

data class AlarmItem(
    val habitId: Int,
    val time: LocalDateTime
)
