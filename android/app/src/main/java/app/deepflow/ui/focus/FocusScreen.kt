package app.deepflow.ui.focus

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FocusScreen(viewModel: FocusViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!uiState.sessionActive) {
            SetupView(
                title = uiState.sessionTitle,
                onTitleChange = viewModel::onTitleChange,
                duration = uiState.initialDuration,
                onDurationChange = viewModel::onDurationChange
            )
        } else {
            TimerDisplay(timeLeft = uiState.timeLeft, title = uiState.sessionTitle)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Controls(
            isRunning = uiState.isRunning,
            sessionActive = uiState.sessionActive,
            onStart = viewModel::start,
            onPause = viewModel::pause,
            onStop = viewModel::stop
        )
    }
}

@Composable
private fun SetupView(
    title: String,
    onTitleChange: (String) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("What are you focusing on?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Duration: $duration minutes")
        Slider(
            value = duration.toFloat(),
            onValueChange = { onDurationChange(it.toInt()) },
            valueRange = 15f..90f,
            steps = 4 // (90-15)/15 - 1 = 4
        )
    }
}

@Composable
private fun TimerDisplay(timeLeft: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = formatTime(timeLeft),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.headlineLarge.fontFamily // Or a specific mono font
        )
    }
}

@Composable
private fun Controls(
    isRunning: Boolean,
    sessionActive: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!sessionActive) {
            Button(onClick = onStart, modifier = Modifier.size(72.dp)) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(36.dp))
            }
        } else {
            Button(onClick = onPause, modifier = Modifier.size(72.dp)) {
                Icon(
                    if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Pause/Resume",
                    modifier = Modifier.size(36.dp)
                )
            }
            Button(onClick = onStop, modifier = Modifier.size(72.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(36.dp))
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
