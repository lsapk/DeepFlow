package com.lovable.dev.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovable.dev.MainApplication
import com.lovable.dev.data.models.Habit
import com.lovable.dev.data.models.Task
import io.github.jan_tennert.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val taskCompletionRate: Float = 0f,
    val activeHabits: Int = 0,
    val isLoading: Boolean = true
)

class DashboardViewModel : ViewModel() {

    private val supabase = MainApplication.supabase

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Fetch tasks
                val tasks = supabase.postgrest["tasks"].select().decodeList<Task>()
                val completedTasks = tasks.count { it.completed }
                val totalTasks = tasks.size
                val taskCompletionRate = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks.toFloat()) * 100 else 0f

                // Fetch habits
                val habits = supabase.postgrest["habits"].select {
                    filter("is_archived", "eq", "false")
                }.decodeList<Habit>()
                val activeHabits = habits.size

                _uiState.value = DashboardUiState(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    taskCompletionRate = taskCompletionRate,
                    activeHabits = activeHabits,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Handle error
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
