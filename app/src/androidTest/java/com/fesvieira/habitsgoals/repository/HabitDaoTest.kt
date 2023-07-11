package com.fesvieira.habitsgoals.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fesvieira.habitsgoals.model.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: HabitDatabase
    private lateinit var dao: HabitDao
    private lateinit var habitItem: Habit

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HabitDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.habitDao()
        habitItem = Habit(1, "name1", 1, 1)
        dao.addHabit(habitItem)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getHabits() = runTest {
        assertThat(dao.getHabits().first()).isEqualTo(listOf(habitItem))
    }

    @Test
    fun insertHabit() = runTest {
        val habits = dao.getHabits().first()

        assertThat(habits).contains(habitItem)
    }

    @Test
    fun removeHabit() = runTest {
        dao.deleteHabit(habitItem)
        val habits = dao.getHabits().first()

        assertThat(habits).doesNotContain(habitItem)
    }

    @Test
    fun getHabitById() = runTest {
        val resultHabit = dao.getHabitById(habitItem.id).first()
        assertThat(resultHabit).isEqualTo(habitItem)
    }

    @Test
    fun updateHabit() = runTest {
        dao.updateHabit(habitItem.copy(name = "aoba"))
        val newHabitItem = dao.getHabitById(habitItem.id).first()

        assertThat(newHabitItem.name).contains("aoba")
    }
}