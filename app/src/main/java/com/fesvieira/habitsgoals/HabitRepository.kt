package com.fesvieira.habitsgoals

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Flow<Habit>

    suspend fun addHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)
}