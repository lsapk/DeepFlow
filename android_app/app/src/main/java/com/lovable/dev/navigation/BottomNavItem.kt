package com.lovable.dev.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Dashboard : BottomNavItem("dashboard_main", Icons.Filled.Dashboard, "Dashboard")
    object Tasks : BottomNavItem("tasks", Icons.Filled.CheckCircle, "Tâches")
    object Habits : BottomNavItem("habits", Icons.Filled.Sync, "Habitudes")
    object Goals : BottomNavItem("goals", Icons.Filled.Flag, "Objectifs")
    object Journal : BottomNavItem("journal", Icons.Filled.Book, "Journal")
    object Focus : BottomNavItem("focus", Icons.Filled.Timer, "Focus")
}
