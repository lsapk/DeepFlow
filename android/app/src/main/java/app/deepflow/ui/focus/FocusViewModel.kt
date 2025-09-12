package app.deepflow.ui.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import io.supabase.postgrest.postgrest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class FocusUiState(
    val timeLeft: Int = 25 * 60, // in seconds
    val initialDuration: Int = 25, // in minutes
    val isRunning: Boolean = false,
    val sessionTitle: String = "",
    val sessionActive: Boolean = false
)

class FocusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime: Long = 0

    fun onDurationChange(duration: Int) {
        if (!uiState.value.sessionActive) {
            _uiState.value = _uiState.value.copy(
                initialDuration = duration,
                timeLeft = duration * 60
            )
        }
    }

    fun onTitleChange(title: String) {
        if (!uiState.value.sessionActive) {
            _uiState.value = _uiState.value.copy(sessionTitle = title)
        }
    }

    fun start() {
        if (_uiState.value.sessionTitle.isBlank()) return

        _uiState.value = _uiState.value.copy(isRunning = true, sessionActive = true)
        sessionStartTime = System.currentTimeMillis()

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000)
                if (_uiState.value.isRunning) {
                    _uiState.value = _uiState.value.copy(timeLeft = _uiState.value.timeLeft - 1)
                }
            }
            // Timer finished
            saveSession(isCompleted = true)
            reset()
        }
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(isRunning = !_uiState.value.isRunning)
    }

    fun stop() {
        saveSession(isCompleted = false)
        reset()
    }

    private fun reset() {
        timerJob?.cancel()
        _uiState.value = FocusUiState(initialDuration = _uiState.value.initialDuration)
    }

    private fun saveSession(isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                val durationToSave = if (isCompleted) {
                    _uiState.value.initialDuration
                } else {
                    // Calculate elapsed time in minutes
                    val elapsedSeconds = (_uiState.value.initialDuration * 60) - _uiState.value.timeLeft
                    (elapsedSeconds / 60)
                }

                if (durationToSave == 0) return@launch

                val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val sessionToSave = mapOf(
                    "title" to _uiState.value.sessionTitle,
                    "duration" to durationToSave,
                    "started_at" to isoFormatter.format(Date(sessionStartTime)),
                    "completed_at" to if (isCompleted) isoFormatter.format(Date()) else null
                )
                SupabaseManager.client.postgrest.from("focus_sessions").insert(sessionToSave)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
