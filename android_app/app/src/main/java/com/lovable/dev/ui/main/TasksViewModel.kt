package com.lovable.dev.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovable.dev.MainApplication
import com.lovable.dev.data.models.Task
import io.github.jan_tennert.supabase.gotrue.auth
import io.github.jan_tennert.supabase.postgrest.postgrest
import io.github.jan_tennert.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class TasksViewModel : ViewModel() {

    private val supabase = MainApplication.supabase

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val tasks = supabase.postgrest["tasks"].select {
                    order("created_at", Order.DESCENDING)
                }.decodeList<Task>()
                _uiState.value = TasksUiState(tasks = tasks, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                // The actual object sent to insert shouldn't contain an 'id' if it's auto-generated
                // For now, we create a map to represent the new task
                val newTask = mapOf(
                    "user_id" to userId,
                    "title" to title
                )
                supabase.postgrest["tasks"].insert(listOf(newTask))
                fetchTasks() // Refresh the list after adding
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            try {
                supabase.postgrest["tasks"].update({
                    set("completed", !task.completed)
                }) {
                    filter("id", "eq", task.id)
                }
                // To avoid a full refresh, we can update the local state directly
                val updatedTasks = _uiState.value.tasks.map {
                    if (it.id == task.id) it.copy(completed = !task.completed) else it
                }
                _uiState.value = _uiState.value.copy(tasks = updatedTasks)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
