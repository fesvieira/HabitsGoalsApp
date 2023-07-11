package com.fesvieira.habitsgoals.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.repository.FakeHabitsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HabitsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FakeHabitsRepository
    private lateinit var viewModel: HabitsViewModel
    private lateinit var habitsList: MutableList<Habit>

    @Before
    fun setup() = runTest {
        habitsList = mutableListOf(
            Habit(1, "habit1", strike = 1, goal = 4),
            Habit(2, "habit2", strike = 1, goal = 4),
            Habit(3, "habit3", strike = 1, goal = 4),
        )
        repository = FakeHabitsRepository(habitsList)
        viewModel = HabitsViewModel(repository)
        viewModel.getHabits()
    }

    @Test
    fun getHabits() = runTest {
        assertThat(viewModel.habits).isNotEmpty()
    }

    @Test
    fun addHabit() = runTest {
        val newHabit = Habit(4,"aoba", 3,9)
        viewModel.addHabit(newHabit)

        assertThat(viewModel.habits).contains(newHabit)
        assertThat(viewModel.habits.size).isEqualTo(4)
    }
}