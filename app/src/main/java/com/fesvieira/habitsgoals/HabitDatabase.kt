package com.fesvieira.habitsgoals

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1, exportSchema = false)

abstract class HabitDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}