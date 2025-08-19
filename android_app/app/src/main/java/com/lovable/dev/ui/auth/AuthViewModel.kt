package com.lovable.dev.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovable.dev.MainApplication
import io.github.jan_tennert.supabase.gotrue.auth
import io.github.jan_tennert.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // A simple flow to represent the result of an auth action
    private val _authResult = MutableStateFlow<Result<Unit>?>(null)
    val authResult = _authResult.asStateFlow()

    private val supabase = MainApplication.supabase

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                _authResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(displayName: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    // Note: Supabase email/password sign-up doesn't include a display name.
                    // This is typically handled in a subsequent profile update step.
                }
                _authResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
