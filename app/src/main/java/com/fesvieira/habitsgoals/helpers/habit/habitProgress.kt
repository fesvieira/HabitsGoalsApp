package com.fesvieira.habitsgoals.helpers.habit

import com.fesvieira.habitsgoals.model.Habit
import java.lang.Float.min

val Habit.progress: String get() {
    return min(((this.strike.toFloat() / this.goal.toFloat()) * 100), 100f).toInt().toString() +
            "%" + " Strike ${this.strike}"
}