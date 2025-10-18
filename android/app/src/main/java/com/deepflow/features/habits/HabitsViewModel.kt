package com.deepflow.features.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepflow.data.model.Habit
import com.deepflow.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _habitsState = MutableStateFlow<HabitsUiState>(HabitsUiState.Loading)
    val habitsState = _habitsState.asStateFlow()

    init {
        loadHabits()
    }

    fun loadHabits() {
        viewModelScope.launch {
            _habitsState.value = HabitsUiState.Loading
            try {
                _habitsState.value = HabitsUiState.Success(habitRepository.getHabits())
            } catch (e: Exception) {
                _habitsState.value = HabitsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun addHabit(title: String, description: String) {
        viewModelScope.launch {
            try {
                habitRepository.createHabit(title, description)
                loadHabits()
            } catch (e: Exception) {
                _habitsState.value = HabitsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun updateHabit(id: String, title: String, description: String) {
        viewModelScope.launch {
            try {
                habitRepository.updateHabit(id, title, description)
                loadHabits()
            } catch (e: Exception) {
                _habitsState.value = HabitsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            try {
                habitRepository.deleteHabit(id)
                loadHabits()
            } catch (e: Exception) {
                _habitsState.value = HabitsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class HabitsUiState {
    object Loading : HabitsUiState()
    data class Success(val habits: List<Habit>) : HabitsUiState()
    data class Error(val message: String) : HabitsUiState()
}