package com.fesvieira.habitsgoals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel(){

    var habits by mutableStateOf(emptyList<Habit>())
    var selectedHabit by mutableStateOf(Habit(0,"",0))

    fun getHabits() {
        viewModelScope.launch {
            habitRepository.getHabits().collect { habitsList ->
                habits = habitsList
            }
        }
    }

    fun getHabitById(habitId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabitById(habitId)
        }
    }

    fun updateHabit(newName: String) {
        selectedHabit = selectedHabit.copy(name = newName)
    }

    fun deleteHabit(habit:Habit) {
         viewModelScope.launch(Dispatchers.IO) {
             habitRepository.deleteHabit(habit)
         }
    }
}