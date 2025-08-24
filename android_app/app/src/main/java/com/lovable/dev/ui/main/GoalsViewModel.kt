package com.lovable.dev.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovable.dev.MainApplication
import com.lovable.dev.data.models.Goal
import io.github.jan_tennert.supabase.gotrue.auth
import io.github.jan_tennert.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoalsViewModel : ViewModel() {

    private val supabaseClient = MainApplication.supabase

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchGoals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val result = supabaseClient.postgrest["goals"].select {
                        filter("user_id", "eq", userId)
                    }.decodeList<Goal>()
                    _goals.value = result
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createGoal(title: String, description: String?) {
        viewModelScope.launch {
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val newGoal = Goal(
                        id = "", // Supabase will generate the ID
                        userId = userId,
                        title = title,
                        description = description
                    )
                    supabaseClient.postgrest["goals"].insert(newGoal)
                    fetchGoals() // Refresh the list
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                supabaseClient.postgrest["goals"].delete {
                    filter("id", "eq", goalId)
                }
                fetchGoals() // Refresh the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
