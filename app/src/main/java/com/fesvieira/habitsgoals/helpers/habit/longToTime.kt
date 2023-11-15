package com.fesvieira.habitsgoals.helpers.habit

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

val Long.longToTime: String get() {
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return "${date.hour}:${date.minute}"
}