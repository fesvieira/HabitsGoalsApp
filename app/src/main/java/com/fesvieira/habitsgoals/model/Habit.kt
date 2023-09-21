package com.fesvieira.habitsgoals.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "HABIT_LIST")
data class Habit(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "strike")
    val strike: Int,
    @ColumnInfo(name = "goal")
    var goal: Int,
    @ColumnInfo(name = "reminder") // Reminder time of the day in minutes
    var reminder: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) {
    companion object {
        val emptyHabit = Habit("", 0, 0)
    }
}
