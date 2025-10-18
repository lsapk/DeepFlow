package com.deepflow.features.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepflow.data.model.JournalEntry
import com.deepflow.data.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _journalState = MutableStateFlow<JournalUiState>(JournalUiState.Loading)
    val journalState = _journalState.asStateFlow()

    init {
        loadJournalEntries()
    }

    fun loadJournalEntries() {
        viewModelScope.launch {
            _journalState.value = JournalUiState.Loading
            try {
                _journalState.value = JournalUiState.Success(journalRepository.getJournalEntries())
            } catch (e: Exception) {
                _journalState.value = JournalUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun addJournalEntry(title: String, content: String) {
        viewModelScope.launch {
            try {
                journalRepository.createJournalEntry(title, content)
                loadJournalEntries()
            } catch (e: Exception) {
                _journalState.value = JournalUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun updateJournalEntry(id: String, title: String, content: String) {
        viewModelScope.launch {
            try {
                journalRepository.updateJournalEntry(id, title, content)
                loadJournalEntries()
            } catch (e: Exception) {
                _journalState.value = JournalUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun deleteJournalEntry(id: String) {
        viewModelScope.launch {
            try {
                journalRepository.deleteJournalEntry(id)
                loadJournalEntries()
            } catch (e: Exception) {
                _journalState.value = JournalUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class JournalUiState {
    object Loading : JournalUiState()
    data class Success(val entries: List<JournalEntry>) : JournalUiState()
    data class Error(val message: String) : JournalUiState()
}