package app.deepflow.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Tasks : BottomNavItem("tasks", "Tasks", Icons.Default.CheckCircle)
    object Habits : BottomNavItem("habits", "Habits", Icons.Default.Repeat)
    object Goals : BottomNavItem("goals", "Goals", Icons.Default.TrackChanges)
    object Focus : BottomNavItem("focus", "Focus", Icons.Default.Timer)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}
