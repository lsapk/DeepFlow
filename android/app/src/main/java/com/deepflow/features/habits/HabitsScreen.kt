package com.deepflow.features.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepflow.data.model.Habit

@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val habitsState by viewModel.habitsState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to add habit screen */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Habit")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = habitsState) {
                is HabitsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HabitsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(state.habits) { habit ->
                            HabitItem(habit = habit)
                        }
                    }
                }
                is HabitsUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.title, style = MaterialTheme.typography.headlineSmall)
            habit.description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}