package com.example.pc02diaz23100503.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc02diaz23100503.data.remote.FirebaseAuthManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val authManager = FirebaseAuthManager()

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authManager.login(_email.value, _password.value)
            _isLoading.value = false
            
            result.onSuccess {
                _loginSuccess.value = true
            }.onFailure {
                _errorMessage.value = it.message ?: "Error al iniciar sesión"
            }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun isUserLoggedIn(): Boolean {
        return authManager.currentUser != null
    }

    fun getCurrentUser() = authManager.currentUser

    fun logout() {
        authManager.logout()
        _loginSuccess.value = false
    }
}
