package app.deepflow.ui.habits

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.deepflow.data.model.HabitFrequency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditScreen(
    habitId: String?,
    onNavigateBack: () -> Unit,
    viewModel: HabitEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadHabit(habitId)
    }

    LaunchedEffect(key1 = uiState.isHabitSaved) {
        if (uiState.isHabitSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (habitId == null) "New Habit" else "Edit Habit") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
         if (uiState.isLoading && habitId != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Frequency", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HabitFrequency.values().forEach { frequency ->
                        FilterChip(
                            selected = uiState.frequency == frequency,
                            onClick = { viewModel.onFrequencyChange(frequency) },
                            label = { Text(frequency.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = viewModel::saveHabit,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !uiState.isLoading
                ) {
                     if(uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Habit")
                    }
                }
            }
        }
    }
}
