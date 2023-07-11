package com.fesvieira.habitsgoals.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.fesvieira.habitsgoals.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM HABIT_LIST")
    fun observeHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM HABIT_LIST ORDER BY id ASC")
    fun getHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM HABIT_LIST WHERE id = :id")
    fun getHabitById(id: Int): Flow<Habit>

    @Insert(onConflict = IGNORE)
    fun addHabit(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)

    @Delete
    fun deleteHabit(habit: Habit)
}