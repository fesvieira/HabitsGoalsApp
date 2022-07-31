package com.fesvieira.habitsgoals.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fesvieira.habitsgoals.model.Habit

@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}