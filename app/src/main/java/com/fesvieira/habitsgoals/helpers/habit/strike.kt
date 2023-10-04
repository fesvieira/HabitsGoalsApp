package com.fesvieira.habitsgoals.helpers.habit

import com.fesvieira.habitsgoals.model.Habit

val Habit.strike: Int get() {
    return this.daysDone.count()
}