package app.deepflow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Priority
import app.deepflow.data.model.Task
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class TaskEditUiState(
    val isLoading: Boolean = false,
    val isTaskSaved: Boolean = false,
    val errorMessage: String? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority? = null
)

class TaskEditViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskEditUiState())
    val uiState = _uiState.asStateFlow()

    private var currentTaskId: String? = null

    fun loadTask(taskId: String?) {
        currentTaskId = taskId
        if (taskId == null) {
            _uiState.value = TaskEditUiState() // Reset for new task
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val task = SupabaseManager.client.postgrest
                    .from("tasks")
                    .select { filter { eq("id", taskId) } }
                    .decodeSingle<Task>()

                _uiState.value = TaskEditUiState(
                    title = task.title,
                    description = task.description ?: "",
                    priority = task.priority,
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

    fun onPriorityChange(priority: Priority?) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun saveTask() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val taskToSave = mapOf(
                    "title" to _uiState.value.title,
                    "description" to _uiState.value.description,
                    "priority" to _uiState.value.priority
                    // Note: Supabase client automatically handles nulls
                )

                if (currentTaskId == null) {
                    // Create new task
                    SupabaseManager.client.postgrest.from("tasks").insert(taskToSave)
                } else {
                    // Update existing task
                    SupabaseManager.client.postgrest.from("tasks").update(taskToSave) {
                        filter { eq("id", currentTaskId!!) }
                    }
                }
                _uiState.value = _uiState.value.copy(isLoading = false, isTaskSaved = true)
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
