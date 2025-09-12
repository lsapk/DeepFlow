package app.deepflow.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import app.deepflow.data.model.Habit
import app.deepflow.data.model.HabitFrequency
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HabitEditUiState(
    val isLoading: Boolean = false,
    val isHabitSaved: Boolean = false,
    val errorMessage: String? = null,
    val name: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY
)

class HabitEditViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HabitEditUiState())
    val uiState = _uiState.asStateFlow()

    private var currentHabitId: String? = null

    fun loadHabit(habitId: String?) {
        currentHabitId = habitId
        if (habitId == null) {
            _uiState.value = HabitEditUiState() // Reset for new habit
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val habit = SupabaseManager.client.postgrest
                    .from("habits")
                    .select { filter { eq("id", habitId) } }
                    .decodeSingle<Habit>()

                _uiState.value = HabitEditUiState(
                    name = habit.name,
                    frequency = habit.frequency,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onFrequencyChange(frequency: HabitFrequency) {
        _uiState.value = _uiState.value.copy(frequency = frequency)
    }

    fun saveHabit() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val habitToSave = mapOf(
                    "name" to _uiState.value.name,
                    "frequency" to _uiState.value.frequency.name.lowercase()
                )

                if (currentHabitId == null) {
                    SupabaseManager.client.postgrest.from("habits").insert(habitToSave)
                } else {
                    SupabaseManager.client.postgrest.from("habits").update(habitToSave) {
                        filter { eq("id", currentHabitId!!) }
                    }
                }
                _uiState.value = _uiState.value.copy(isLoading = false, isHabitSaved = true)
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
