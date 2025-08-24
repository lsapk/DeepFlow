package com.lovable.dev.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Goals : Screen("goals")
    object AddGoal : Screen("add_goal")
    // Add other screens here as they are created
}
