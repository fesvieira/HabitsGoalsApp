package com.fesvieira.habitsgoals.helpers

val Int.minutesToHours: String get() {
    val hours = this / 60
    val hoursString = if (hours <= 9) "0$hours" else "$hours"
    val minutes = this % 60
    val minutesString = if (minutes <= 9) "0$minutes" else "$minutes"

    return "$hoursString:$minutesString"
}