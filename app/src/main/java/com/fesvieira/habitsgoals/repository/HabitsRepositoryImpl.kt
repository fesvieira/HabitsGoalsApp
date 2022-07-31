package com.fesvieira.habitsgoals.repository

import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.repository.HabitDao
import com.fesvieira.habitsgoals.repository.HabitRepository
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