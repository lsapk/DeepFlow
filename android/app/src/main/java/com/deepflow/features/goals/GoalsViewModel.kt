package com.deepflow.features.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepflow.data.model.Goal
import com.deepflow.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _goalsState = MutableStateFlow<GoalsUiState>(GoalsUiState.Loading)
    val goalsState = _goalsState.asStateFlow()

    init {
        loadGoals()
    }

    fun loadGoals() {
        viewModelScope.launch {
            _goalsState.value = GoalsUiState.Loading
            try {
                _goalsState.value = GoalsUiState.Success(goalRepository.getGoals())
            } catch (e: Exception) {
                _goalsState.value = GoalsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun addGoal(title: String, description: String) {
        viewModelScope.launch {
            try {
                goalRepository.createGoal(title, description)
                loadGoals()
            } catch (e: Exception) {
                _goalsState.value = GoalsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun updateGoal(id: String, title: String, description: String, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                goalRepository.updateGoal(id, title, description, isCompleted)
                loadGoals()
            } catch (e: Exception) {
                _goalsState.value = GoalsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun deleteGoal(id: String) {
        viewModelScope.launch {
            try {
                goalRepository.deleteGoal(id)
                loadGoals()
            } catch (e: Exception) {
                _goalsState.value = GoalsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class GoalsUiState {
    object Loading : GoalsUiState()
    data class Success(val goals: List<Goal>) : GoalsUiState()
    data class Error(val message: String) : GoalsUiState()
}