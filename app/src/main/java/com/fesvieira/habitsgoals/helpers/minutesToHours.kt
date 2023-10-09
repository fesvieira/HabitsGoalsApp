package com.fesvieira.habitsgoals.helpers

import android.content.Context
import android.text.format.DateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun Int.minutesToHours(context: Context): String {
    return LocalTime.of(this / 60, this % 60).format(
        DateTimeFormatter.ofPattern(
            if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm a"
        )
    )
}