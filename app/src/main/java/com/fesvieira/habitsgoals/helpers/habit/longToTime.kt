package com.fesvieira.habitsgoals.helpers.habit

import android.content.Context
import android.text.format.DateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.longToTime(context: Context): String {
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val is24 = DateFormat.is24HourFormat(context)

    return date.toLocalTime().format(DateTimeFormatter.ofPattern(if (is24) "HH:mm" else "hh:mm a"))
}