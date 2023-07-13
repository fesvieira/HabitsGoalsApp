package com.fesvieira.habitsgoals.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.repository.FakeHabitsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HabitsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    private lateinit var repository: FakeHabitsRepository
    private lateinit var viewModel: HabitsViewModel
    private lateinit var habitsList: MutableList<Habit>

    @Before
    fun setup() = scope.runTest {
        Dispatchers.setMain(dispatcher)
        habitsList = mutableListOf(
            Habit(1, "habit1", strike = 1, goal = 4),
            Habit(2, "habit2", strike = 1, goal = 4),
            Habit(3, "habit3", strike = 1, goal = 4),
        )
        repository = FakeHabitsRepository(habitsList)
        viewModel = HabitsViewModel(repository)
        viewModel.getHabits()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getHabits() {
        assertThat(viewModel.habits).isNotEmpty()
    }

    @Test
    fun addHabit() {
        val newHabit = Habit(4,"aoba", 3,9)
        viewModel.addHabit(newHabit)

        assertThat(viewModel.habits).contains(newHabit)
        assertThat(viewModel.habits.size).isEqualTo(4)
    }
}