package com.fesvieira.habitsgoals.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fesvieira.habitsgoals.model.DateStampsConverter
import com.fesvieira.habitsgoals.model.Habit

@Database(entities = [Habit::class], version = 1, exportSchema = false)
@TypeConverters(DateStampsConverter::class)
abstract class HabitDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}