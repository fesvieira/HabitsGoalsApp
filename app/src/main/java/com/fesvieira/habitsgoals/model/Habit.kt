package com.fesvieira.habitsgoals.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter


@Entity(tableName = "HABIT_LIST")
data class Habit(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "daysDone")
    var daysDone: List<Long>,
    @ColumnInfo(name = "goal")
    var goal: Int,
    @ColumnInfo(name = "reminder") // Reminder time of the day in minutes
    var reminder: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) {
    companion object {
        val emptyHabit = Habit("", emptyList(), 0)
    }
}

@ProvidedTypeConverter
class DateStampsConverter {
    @TypeConverter
    fun stringToLongs(value: String): List<Long> {
        return value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.map { it.toLong() }
    }

    @TypeConverter
    fun longsToString(cl: List<Long>): String {
        var value = ""
        for (lang in cl) value += "$lang,"
        return value
    }
}
