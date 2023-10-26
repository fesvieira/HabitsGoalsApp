package com.fesvieira.habitsgoals.helpers.alarmmanager

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)
