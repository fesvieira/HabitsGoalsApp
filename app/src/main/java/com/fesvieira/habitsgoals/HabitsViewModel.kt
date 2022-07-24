package com.fesvieira.habitsgoals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

        viewModelScope.launch(Dispatchers.IO) {
            selectedHabit = selectedHabit.copy(name = newName)
            habitRepository.updateHabit(selectedHabit)
        }

    }

    fun deleteHabit(habit:Habit) {
         viewModelScope.launch(Dispatchers.IO) {
             habitRepository.deleteHabit(habit)
         }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch (Dispatchers.IO){
            habitRepository.addHabit(habit)
        }
    }
}