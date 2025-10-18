package com.deepflow.features

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.deepflow.navigation.BottomNavigationBar
import com.deepflow.navigation.MainScreenNavGraph

@Composable
fun MainScreen(onSignOut: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        MainScreenNavGraph(navController = navController)
    }
}