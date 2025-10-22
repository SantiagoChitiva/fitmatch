package com.example.fitmatch.presentation.ui.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmatch.presentation.ui.screens.auth.state.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


//ViewModel para la pantalla de Registro.
// Maneja toda la lógica de negocio y validación del formulario.

class RegisterViewModel : ViewModel() {

    // ========== ESTADO ==========
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // EVENTOS DESDE LA UI

    fun onEmailChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = value,
                errorMessage = null,
                isRegisterEnabled = RegisterUiState.isValidForm(
                    email = value,
                    password = currentState.password,
                    fullName = currentState.fullName,
                    birthDate = currentState.birthDate,
                    role = currentState.selectedRole
                )
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = value,
                errorMessage = null,
                isRegisterEnabled = RegisterUiState.isValidForm(
                    email = currentState.email,
                    password = value,
                    fullName = currentState.fullName,
                    birthDate = currentState.birthDate,
                    role = currentState.selectedRole
                )
            )
        }
    }

    fun onFullNameChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                fullName = value,
                errorMessage = null,
                isRegisterEnabled = RegisterUiState.isValidForm(
                    email = currentState.email,
                    password = currentState.password,
                    fullName = value,
                    birthDate = currentState.birthDate,
                    role = currentState.selectedRole
                )
            )
        }
    }

    fun onBirthDateChanged(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                birthDate = value,
                errorMessage = null,
                isRegisterEnabled = RegisterUiState.isValidForm(
                    email = currentState.email,
                    password = currentState.password,
                    fullName = currentState.fullName,
                    birthDate = value,
                    role = currentState.selectedRole
                )
            )
        }
    }

    fun onCityChanged(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    fun onGenderSelected(gender: String) {
        _uiState.update {
            it.copy(
                selectedGender = gender,
                isGenderDropdownExpanded = false
            )
        }
    }

    fun onGenderDropdownToggle() {
        _uiState.update {
            it.copy(isGenderDropdownExpanded = !it.isGenderDropdownExpanded)
        }
    }

    fun onRoleSelected(role: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedRole = role,
                isRegisterEnabled = RegisterUiState.isValidForm(
                    email = currentState.email,
                    password = currentState.password,
                    fullName = currentState.fullName,
                    birthDate = currentState.birthDate,
                    role = role
                )
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

    /**
     * Evento: el usuario presionó "Registrarse".
     * onSuccess recibe el rol para navegar a la pantalla correspondiente.
     */
    fun onRegisterClick(onSuccess: (role: String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // TODO: Reemplazar con llamada real al repositorio
            // Ejemplo: val result = authRepository.register(...)

            // Simulación de registro
            delay(1500)

            val currentState = _uiState.value
            val isSuccess = simulateRegister(currentState)

            if (isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                // Pasar el rol al callback de navegación
                onSuccess(currentState.selectedRole)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al registrar. Intenta nuevamente."
                    )
                }
            }
        }
    }

    // LÓGICA DE NEGOCIO PRIVADA

    // Simulación de registro (reemplazar con repositorio real).

    private fun simulateRegister(state: RegisterUiState): Boolean {
        // Validación adicional (ejemplo: email único)
        return state.email.contains("@") && state.password.length >= 8
    }
}