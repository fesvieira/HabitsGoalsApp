package com.fesvieira.habitsgoals.helpers

import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.repository.HabitDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeDao: HabitDao {
    override fun getHabits(): Flow<List<Habit>> { return flow {} }
    override fun getHabitById(id: Int): Flow<Habit> { return flow {} }
    override fun addHabit(habit: Habit) {}
    override fun updateHabit(habit: Habit) {}
    override fun deleteHabit(habit: Habit) {}
}