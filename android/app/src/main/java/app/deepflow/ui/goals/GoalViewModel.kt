package app.deepflow.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Goal
import app.deepflow.data.model.SubObjective
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GoalUiState(
    val ongoingGoals: List<Goal> = emptyList(),
    val completedGoals: List<Goal> = emptyList(),
    val subObjectives: Map<String, List<SubObjective>> = emptyMap(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class GoalViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchGoals()
    }

    fun fetchGoals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val allGoals = SupabaseManager.client.postgrest
                    .from("goals")
                    .select()
                    .decodeList<Goal>()

                val goalIds = allGoals.map { it.id }
                val allSubObjectives = SupabaseManager.client.postgrest
                    .from("subobjectives")
                    .select()
                    .inList("goal_id", goalIds)
                    .decodeList<SubObjective>()

                _uiState.value = GoalUiState(
                    ongoingGoals = allGoals.filter { !it.completed },
                    completedGoals = allGoals.filter { it.completed },
                    subObjectives = allSubObjectives.groupBy { it.goalId },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    // Placeholder for CRUD
    fun deleteGoal(goal: Goal) { /* ... */ }
}
