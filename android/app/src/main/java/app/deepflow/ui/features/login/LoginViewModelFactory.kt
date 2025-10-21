package app.deepflow.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.deepflow.data.repository.UserRepository

class LoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
