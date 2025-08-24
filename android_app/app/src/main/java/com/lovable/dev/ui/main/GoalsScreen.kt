package com.lovable.dev.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.lovable.dev.ui.components.GoalItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onAddGoal: () -> Unit
) {
    val goals by viewModel.goals.collectAsState()
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ongoing", "Completed")

    LaunchedEffect(Unit) {
        viewModel.fetchGoals()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddGoal) {
                Icon(Icons.Filled.Add, contentDescription = "Add Goal")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            LazyColumn {
                val filteredGoals = goals.filter { goal ->
                    if (tabIndex == 0) !goal.completed else goal.completed
                }
                items(filteredGoals) { goal ->
                    GoalItem(goal = goal)
                }
            }
        }
    }
}
