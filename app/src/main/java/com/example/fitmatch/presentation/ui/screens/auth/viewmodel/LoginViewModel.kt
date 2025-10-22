package com.example.fitmatch.presentation.ui.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmatch.presentation.ui.screens.auth.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
//
//
//  ViewModel para la pantalla de Login.
//  Contiene toda la lógica de negocio y el estado de la pantalla.
//
class LoginViewModel : ViewModel() {

    // ESTADO
    // Estado privado y mutable - la única fuente de verdad
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado público e inmutable - la UI lo observa
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ========== EVENTOS DESDE LA UI ==========

    //El usuario modifica el cambio de email o telefono
    fun onEmailOrPhoneChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                emailOrPhone = value,
                errorMessage = null, // Limpiar error al escribir
                isLoginEnabled = LoginUiState.isValidForm(value, currentState.password)
            )
        }
    }

    // el usuario cambia el campo de contraseña.

    fun onPasswordChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = value,
                errorMessage = null,
                isLoginEnabled = LoginUiState.isValidForm(currentState.emailOrPhone, value)
            )
        }
    }


   // el usuario cambio la visibilidad de la contraseña
    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

     //Evento: el usuario presionó el botón "Continuar" (Login).
     //Aquí iría la lógica real de autenticación (llamada al repositorio).

    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // TODO: Reemplazar con llamada real al repositorio
            // Ejemplo: val result = authRepository.login(email, password)

            // Simulación de llamada de red
            delay(1500)

            // Simulación de validación
            val currentState = _uiState.value
            val isValidCredentials = simulateLogin(
                currentState.emailOrPhone,
                currentState.password
            )

            if (isValidCredentials) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess() // Navegar a la siguiente pantalla
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Credenciales incorrectas. Intenta de nuevo."
                    )
                }
            }
        }
    }

     //Evento: el usuario presionó "Olvidé mi contraseña".

    fun onForgotPasswordClick() {
        // TODO: Implementar navegación o lógica de recuperación
        // Por ahora solo limpiamos el error
        _uiState.update { it.copy(errorMessage = null) }
    }
    //auth
    private fun simulateLogin(email: String, password: String): Boolean {
        // acepta cualquier email con pass >= 8 caracteres
        return email.contains("@") || email.length >= 10
    }
}