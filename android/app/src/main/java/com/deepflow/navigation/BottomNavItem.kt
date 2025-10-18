package com.deepflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {
    object Goals : BottomNavItem("Goals", Icons.Default.CheckCircle, "goals")
    object Habits : BottomNavItem("Habits", Icons.Default.Star, "habits")
    object Journal : BottomNavItem("Journal", Icons.Default.Create, "journal")
}