package app.deepflow.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Habit
import app.deepflow.data.model.HabitCompletion
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HabitUiState(
    val activeHabits: List<Habit> = emptyList(),
    val archivedHabits: List<Habit> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HabitViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHabits()
    }

    fun fetchHabits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val allHabits = SupabaseManager.client.postgrest
                    .from("habits")
                    .select()
                    .decodeList<Habit>()

                val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val completions = SupabaseManager.client.postgrest
                    .from("habit_completions")
                    .select()
                    .eq("completed_date", todayString)
                    .decodeList<HabitCompletion>()

                val completedHabitIds = completions.map { it.habitId }.toSet()

                val habitsWithCompletionStatus = allHabits.map {
                    it.copy(isCompletedToday = completedHabitIds.contains(it.id))
                }

                _uiState.value = HabitUiState(
                    activeHabits = habitsWithCompletionStatus.filter { !it.isArchived },
                    archivedHabits = habitsWithCompletionStatus.filter { !it.isArchived },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun toggleHabitCompletion(habit: Habit) {
        viewModelScope.launch {
            try {
                val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                if (habit.isCompletedToday) {
                    // Delete completion record
                    SupabaseManager.client.postgrest.from("habit_completions").delete {
                        filter {
                            eq("habit_id", habit.id)
                            eq("completed_date", todayString)
                        }
                    }
                    // Decrement streak
                    SupabaseManager.client.postgrest.from("habits").update(
                        mapOf("streak" to habit.streak - 1)
                    ) { filter { eq("id", habit.id) } }
                } else {
                    // Insert completion record
                    // The user_id is set by RLS, and id is auto-generated
                    SupabaseManager.client.postgrest.from("habit_completions").insert(
                        mapOf("habit_id" to habit.id, "completed_date" to todayString)
                    )
                    // Increment streak and update last completed date
                    SupabaseManager.client.postgrest.from("habits").update(
                        mapOf(
                            "streak" to habit.streak + 1,
                            "last_completed_at" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
                        )
                    ) { filter { eq("id", habit.id) } }
                }
                fetchHabits() // Refresh state
            } catch (e: Exception) {
                // Handle error
                 _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    // Placeholders for other operations
    fun deleteHabit(habit: Habit) { /* ... */ }
    fun archiveHabit(habit: Habit) { /* ... */ }
}
