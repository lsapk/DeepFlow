package com.lovable.dev.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lovable.dev.data.models.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen() {
    val viewModel: HabitsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var newHabitTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habitudes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (newHabitTitle.isNotBlank()) {
                    viewModel.addHabit(newHabitTitle)
                    newHabitTitle = ""
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter une habitude")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = newHabitTitle,
                onValueChange = { newHabitTitle = it },
                label = { Text("Nouvelle habitude...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erreur: ${uiState.error}")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.habits, key = { it.id }) { habit ->
                        HabitItem(habit = habit, onCompleteClick = {
                            viewModel.completeHabitToday(habit)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onCompleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = habit.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Série : ${habit.streak ?: 0} jours",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onCompleteClick) {
                Icon(Icons.Default.Done, contentDescription = "Marquer comme fait")
            }
        }
    }
}
