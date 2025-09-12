package app.deepflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import app.deepflow.ui.auth.AuthState
import app.deepflow.ui.auth.AuthViewModel
import app.deepflow.ui.navigation.AppNavHost
import app.deepflow.ui.navigation.AppRoutes

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeepFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authState by authViewModel.authState.collectAsState()

                    // This effect will react to changes in authState
                    LaunchedEffect(authState) {
                        if (authState is AuthState.Authenticated) {
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.LOGIN) { inclusive = true }
                            }
                        } else {
                            // On logout or error, return to login screen
                            navController.navigate(AppRoutes.LOGIN) {
                                popUpTo(AppRoutes.HOME) { inclusive = true }
                            }
                        }
                    }

                    AppNavHost(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

// I will create a basic theme for compose
@Composable
fun DeepFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
