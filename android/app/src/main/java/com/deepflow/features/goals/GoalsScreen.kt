package com.deepflow.features.goals

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
import com.deepflow.data.model.Goal

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val goalsState by viewModel.goalsState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to add goal screen */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Goal")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = goalsState) {
                is GoalsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is GoalsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(state.goals) { goal ->
                            GoalItem(goal = goal)
                        }
                    }
                }
                is GoalsUiState.Error -> {
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
fun GoalItem(goal: Goal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = goal.title, style = MaterialTheme.typography.headlineSmall)
            goal.description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}