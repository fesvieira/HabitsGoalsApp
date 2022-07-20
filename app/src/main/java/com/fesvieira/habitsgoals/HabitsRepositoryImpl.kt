package com.fesvieira.habitsgoals

import kotlinx.coroutines.flow.Flow

class HabitsRepositoryImpl(
    private val habitDao: HabitDao
) : HabitRepository {
    override suspend fun getHabits(): Flow<List<Habit>> {
        return habitDao.getHabits()
    }

    override suspend fun getHabitById(id: Int): Flow<Habit> {
        return habitDao.getHabitById(id)
    }

    override suspend fun addHabit(habit: Habit) {
        return habitDao.addHabit(habit = habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        return habitDao.updateHabit(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        return habitDao.deleteHabit(habit)
    }
}