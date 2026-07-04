package com.example.pc02diaz23100503.presentation.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc02diaz23100503.data.remote.FirebaseAuthManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val authManager = FirebaseAuthManager()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onRegisterClick() {
        if (!isValidEmail(email)) {
            viewModelScope.launch { _uiEvent.emit(RegisterUiEvent.ShowSnackbar("Correo inválido")) }
            return
        }
        if (password.length < 6) {
            viewModelScope.launch { _uiEvent.emit(RegisterUiEvent.ShowSnackbar("La contraseña debe tener al menos 6 caracteres")) }
            return
        }
        if (password != confirmPassword) {
            viewModelScope.launch { _uiEvent.emit(RegisterUiEvent.ShowSnackbar("Las contraseñas no coinciden")) }
            return
        }

        isLoading = true
        viewModelScope.launch {
            val result = authManager.register(email, password)
            isLoading = false
            result.onSuccess {
                _uiEvent.emit(RegisterUiEvent.Success)
            }.onFailure {
                _uiEvent.emit(RegisterUiEvent.ShowSnackbar(it.message ?: "Error al registrarse"))
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    sealed class RegisterUiEvent {
        data class ShowSnackbar(val message: String) : RegisterUiEvent()
        object Success : RegisterUiEvent()
    }
}
