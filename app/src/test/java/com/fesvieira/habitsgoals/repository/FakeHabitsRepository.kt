package com.fesvieira.habitsgoals.repository

import com.fesvieira.habitsgoals.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeHabitsRepository(private val habitsList: MutableList<Habit>): HabitRepository {
    override suspend fun getHabits(): Flow<List<Habit>> = flow {
        emit(habitsList)
    }

    override suspend fun getHabitById(id: Int): Flow<Habit> = flow {
        emit(habitsList.first { it.id == id })
    }

    override suspend fun addHabit(habit: Habit) {
        habitsList.add(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        val index = habitsList.indexOfFirst { it.id == habit.id }
        if (index != -1) habitsList[index] = habit
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitsList.remove(habit)
    }
}