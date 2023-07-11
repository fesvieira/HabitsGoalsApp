package com.fesvieira.habitsgoals.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fesvieira.habitsgoals.getOrAwaitValue
import com.fesvieira.habitsgoals.model.Habit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
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

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HabitDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.habitDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertHabit() = runTest {
        val habitItem = Habit(1, "name1", 1, 1)
        dao.addHabit(habitItem)
        val habits = dao.observeHabits().getOrAwaitValue()

        assertThat(habits).contains(habitItem)
    }

    @Test
    fun removeHabit() = runTest {
        val habitItem = Habit(1, "name1", 1, 1)
        dao.addHabit(habitItem)
        dao.deleteHabit(habitItem)
        val habits = dao.observeHabits().getOrAwaitValue()

        assertThat(habits).doesNotContain(habitItem)
    }
}