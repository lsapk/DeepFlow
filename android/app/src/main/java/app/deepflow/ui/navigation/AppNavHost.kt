package app.deepflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.deepflow.ui.auth.LoginScreen
import app.deepflow.ui.auth.RegisterScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import app.deepflow.ui.auth.AuthViewModel
import androidx.compose.material3.Text
import androidx.navigation.NavHostController

// Define the routes
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.signIn(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterClick = { email, password ->
                    authViewModel.signUp(email, password)
                },
                onNavigateToLogin = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.HOME) {
            app.deepflow.ui.home.HomeScreen(authViewModel = authViewModel)
        }
    }
}
