package com.fesvieira.habitsgoals.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HABIT_LIST")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "strike")
    val strike: Int,
    @ColumnInfo(name = "goal")
    var goal: Int,
    @ColumnInfo(name = "reminder")
    var reminder: Boolean = false,
) {
    companion object {
        val emptyHabit = Habit(0, "", 0, 0)
    }
}
