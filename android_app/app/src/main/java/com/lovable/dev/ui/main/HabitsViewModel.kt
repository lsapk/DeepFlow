package com.lovable.dev.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovable.dev.MainApplication
import com.lovable.dev.data.models.Habit
import io.github.jan_tennert.supabase.gotrue.auth
import io.github.jan_tennert.supabase.postgrest.postgrest
import io.github.jan_tennert.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HabitsUiState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HabitsViewModel : ViewModel() {

    private val supabase = MainApplication.supabase

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHabits()
    }

    private fun fetchHabits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val habits = supabase.postgrest["habits"].select {
                    order("created_at", Order.DESCENDING)
                }.decodeList<Habit>()
                _uiState.value = HabitsUiState(habits = habits, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun addHabit(title: String, frequency: String = "daily") {
        viewModelScope.launch {
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val newHabit = mapOf(
                    "user_id" to userId,
                    "title" to title,
                    "frequency" to frequency,
                    "target" to 1,
                    "streak" to 0
                )
                supabase.postgrest["habits"].insert(listOf(newHabit))
                fetchHabits() // Refresh list
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun completeHabitToday(habit: Habit) {
        viewModelScope.launch {
            // This is a simplified logic. A real implementation would check the last
            // completion date to correctly update the streak.
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val completion = mapOf(
                     "user_id" to userId,
                     "habit_id" to habit.id
                )
                 supabase.postgrest["habit_completions"].insert(listOf(completion))

                // Update the habit's streak and last completed date
                supabase.postgrest["habits"].update({
                    set("streak", (habit.streak ?: 0) + 1)
                    set("last_completed_at", "now()")
                }) {
                    filter("id", "eq", habit.id)
                }
                fetchHabits()
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
