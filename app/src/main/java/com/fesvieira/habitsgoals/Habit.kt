package com.fesvieira.habitsgoals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HABIT_LIST")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val strike: Int
)
