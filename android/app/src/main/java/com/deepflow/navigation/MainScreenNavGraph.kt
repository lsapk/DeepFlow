package com.deepflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deepflow.features.goals.GoalsScreen
import com.deepflow.features.habits.HabitsScreen
import com.deepflow.features.journal.JournalScreen

@Composable
fun MainScreenNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Goals.screen_route) {
        composable(BottomNavItem.Goals.screen_route) {
            GoalsScreen()
        }
        composable(BottomNavItem.Habits.screen_route) {
            HabitsScreen()
        }
        composable(BottomNavItem.Journal.screen_route) {
            JournalScreen()
        }
    }
}