package app.deepflow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Habit
import app.deepflow.data.model.Task
import app.deepflow.data.model.FocusSession
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val taskCompletionRate: Float = 0f,
    val totalFocusTimeMinutes: Int = 0,
    val activeHabits: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Fetch all data in parallel
                val tasks = SupabaseManager.client.postgrest.from("tasks").select().decodeList<Task>()
                val habits = SupabaseManager.client.postgrest.from("habits").select().decodeList<Habit>()
                val focusSessions = SupabaseManager.client.postgrest.from("focus_sessions").select().decodeList<FocusSession>()

                // Perform calculations
                val completed = tasks.count { it.completed }
                val totalFocus = focusSessions.sumOf { it.duration }

                _uiState.value = DashboardUiState(
                    totalTasks = tasks.size,
                    completedTasks = completed,
                    taskCompletionRate = if (tasks.isNotEmpty()) (completed.toFloat() / tasks.size) * 100 else 0f,
                    totalFocusTimeMinutes = totalFocus,
                    activeHabits = habits.size,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load dashboard data."
                )
            }
        }
    }
}
