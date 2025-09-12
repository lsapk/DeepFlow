package app.deepflow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Subtask
import app.deepflow.data.model.Task
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TasksUiState(
    val pendingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val subtasks: Map<String, List<Subtask>> = emptyMap(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class TaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchTasksAndSubtasks()
    }

    fun fetchTasksAndSubtasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val allTasks = SupabaseManager.client.postgrest
                    .from("tasks")
                    .select()
                    .decodeList<Task>()
                    .sortedByDescending { it.priority } // Basic sorting

                val taskIds = allTasks.map { it.id }
                val allSubtasks = SupabaseManager.client.postgrest
                    .from("subtasks")
                    .select()
                    .inList("parent_task_id", taskIds)
                    .decodeList<Subtask>()

                _uiState.value = TasksUiState(
                    pendingTasks = allTasks.filter { !it.completed },
                    completedTasks = allTasks.filter { it.completed },
                    subtasks = allSubtasks.groupBy { it.parentTaskId },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load tasks."
                )
            }
        }
    }

    fun toggleTaskComplete(task: Task) {
        viewModelScope.launch {
            try {
                SupabaseManager.client.postgrest
                    .from("tasks")
                    .update(mapOf("completed" to !task.completed)) {
                        filter { eq("id", task.id) }
                    }
                // Refresh data to reflect the change
                fetchTasksAndSubtasks()
            } catch (e: Exception) {
                // Handle error, maybe show a snackbar
            }
        }
    }

    fun deleteTask(task: Task) {
         viewModelScope.launch {
            try {
                SupabaseManager.client.postgrest
                    .from("tasks")
                    .delete {
                        filter { eq("id", task.id) }
                    }
                fetchTasksAndSubtasks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Add placeholders for other CRUD operations
    fun addTask(task: Task) { /* ... */ }
    fun updateTask(task: Task) { /* ... */ }
    fun addSubtask(subtask: Subtask) { /* ... */ }
    fun updateSubtask(subtask: Subtask) { /* ... */ }
    fun deleteSubtask(subtask: Subtask) { /* ... */ }
}
