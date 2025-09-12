package app.deepflow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.deepflow.data.SupabaseManager
import io.supabase.gotrue.auth
import io.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// This sealed class will represent the state of the authentication process
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    init {
        // Check if a user is already logged in when the ViewModel is created
        viewModelScope.launch {
            val session = SupabaseManager.client.auth.session()
            if (session != null) {
                _authState.value = AuthState.Authenticated
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseManager.client.auth.signUp(Email) {
                    this.email = email
                    this.password = password
                }
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseManager.client.auth.signIn(Email) {
                    this.email = email
                    this.password = password
                }
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                SupabaseManager.client.auth.signOut()
                _authState.value = AuthState.Idle
            } catch (e: Exception) {
                 // Even if sign-out fails, we can treat it as logged out on the client side
                _authState.value = AuthState.Error(e.message ?: "Sign out failed")
            }
        }
    }
}
