package com.deepflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deepflow.auth.AuthState
import com.deepflow.auth.AuthViewModel
import com.deepflow.auth.LoginScreen
import com.deepflow.auth.SignUpScreen
import com.deepflow.features.MainScreen

@Composable
fun NavGraph(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("main") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("main") {
            MainScreen(
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}