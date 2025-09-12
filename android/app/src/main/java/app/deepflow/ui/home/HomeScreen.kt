package app.deepflow.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import app.deepflow.ui.auth.AuthViewModel
import app.deepflow.ui.focus.FocusScreen
import app.deepflow.ui.goals.GoalEditScreen
import app.deepflow.ui.goals.GoalsScreen
import app.deepflow.ui.habits.HabitEditScreen
import app.deepflow.ui.habits.HabitsScreen
import app.deepflow.ui.settings.SettingsScreen
import app.deepflow.ui.tasks.TaskEditScreen
import app.deepflow.ui.tasks.TasksScreen

object HomeRoutes {
    const val DASHBOARD = "dashboard"
    const val TASKS = "tasks"
    const val TASK_EDIT = "task_edit"
    const val HABITS = "habits"
    const val HABIT_EDIT = "habit_edit"
    const val GOALS = "goals"
    const val GOAL_EDIT = "goal_edit"
    const val FOCUS = "focus"
    const val SETTINGS = "settings"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeNavHost(navController = navController, authViewModel = authViewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Tasks,
        BottomNavItem.Habits,
        BottomNavItem.Goals,
        BottomNavItem.Focus,
        BottomNavItem.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun HomeNavHost(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = HomeRoutes.DASHBOARD) {
        composable(HomeRoutes.DASHBOARD) { DashboardScreen() }

        // Tasks Navigation
        composable(HomeRoutes.TASKS) {
            TasksScreen(
                onNavigateToEditTask = { taskId ->
                    val route = "${HomeRoutes.TASK_EDIT}${taskId?.let { "/$it" } ?: ""}"
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = "${HomeRoutes.TASK_EDIT}/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            TaskEditScreen(
                taskId = backStackEntry.arguments?.getString("taskId"),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Habits Navigation
        composable(HomeRoutes.HABITS) {
            HabitsScreen(
                onNavigateToEditHabit = { habitId ->
                    val route = "${HomeRoutes.HABIT_EDIT}${habitId?.let { "/$it" } ?: ""}"
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = "${HomeRoutes.HABIT_EDIT}/{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            HabitEditScreen(
                habitId = backStackEntry.arguments?.getString("habitId"),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Goals Navigation
        composable(HomeRoutes.GOALS) {
            GoalsScreen(
                onNavigateToEditGoal = { goalId ->
                    val route = "${HomeRoutes.GOAL_EDIT}${goalId?.let { "/$it" } ?: ""}"
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = "${HomeRoutes.GOAL_EDIT}/{goalId}",
            arguments = listOf(navArgument("goalId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            GoalEditScreen(
                goalId = backStackEntry.arguments?.getString("goalId"),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(HomeRoutes.FOCUS) { FocusScreen() }
        composable(HomeRoutes.SETTINGS) { SettingsScreen(authViewModel = authViewModel) }
    }
}

// --- Screens ---

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    // ... (Implementation from before, assuming it's here)
}

@Composable
fun DashboardMetricCard(title: String, value: String, subtitle: String) {
    // ... (Implementation from before, assuming it's here)
}
