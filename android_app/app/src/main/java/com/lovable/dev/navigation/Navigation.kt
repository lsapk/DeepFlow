package com.lovable.dev.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lovable.dev.Greeting
import com.lovable.dev.ui.auth.AuthViewModel
import com.lovable.dev.ui.auth.LoginScreen
import com.lovable.dev.ui.auth.RegisterScreen
import com.lovable.dev.ui.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authResult by authViewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        if (authResult?.isSuccess == true) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
        // You can also handle authResult.isFailure here, e.g., show a toast
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            val isLoading by authViewModel.isLoading.collectAsState()
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                isLoading = isLoading
            )
        }
        composable(Screen.Register.route) {
            val isLoading by authViewModel.isLoading.collectAsState()
            RegisterScreen(
                onRegisterClick = { _, email, password ->
                    authViewModel.register(email, password, email)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                isLoading = isLoading
            )
        }
        composable(Screen.Dashboard.route) {
            MainScreen()
        }
    }
}
