package app.deepflow.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Goal
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GoalEditUiState(
    val isLoading: Boolean = false,
    val isGoalSaved: Boolean = false,
    val errorMessage: String? = null,
    val title: String = "",
    val description: String = ""
)

class GoalEditViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GoalEditUiState())
    val uiState = _uiState.asStateFlow()

    private var currentGoalId: String? = null

    fun loadGoal(goalId: String?) {
        currentGoalId = goalId
        if (goalId == null) {
            _uiState.value = GoalEditUiState()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val goal = SupabaseManager.client.postgrest
                    .from("goals")
                    .select { filter { eq("id", goalId) } }
                    .decodeSingle<Goal>()

                _uiState.value = GoalEditUiState(
                    title = goal.title,
                    description = goal.description ?: "",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun saveGoal() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val goalToSave = mapOf(
                    "title" to _uiState.value.title,
                    "description" to _uiState.value.description
                )

                if (currentGoalId == null) {
                    SupabaseManager.client.postgrest.from("goals").insert(goalToSave)
                } else {
                    SupabaseManager.client.postgrest.from("goals").update(goalToSave) {
                        filter { eq("id", currentGoalId!!) }
                    }
                }
                _uiState.value = _uiState.value.copy(isLoading = false, isGoalSaved = true)
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
