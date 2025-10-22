package com.example.fitmatch.presentation.ui.screens.cliente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmatch.presentation.ui.screens.cliente.state.PaymentMethod
import com.example.fitmatch.presentation.ui.screens.cliente.state.ProfileUiState
import com.example.fitmatch.presentation.ui.screens.cliente.state.ShippingAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ProfileViewModel : ViewModel() {

    // Estados
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }
    //acciones

    // Manejar selección/deselección de tallas
    fun onToggleSize(size: String) {
        _uiState.update { currentState ->
            val updatedSizes = if (currentState.preferredSizes.contains(size)) {
                currentState.preferredSizes - size
            } else {
                currentState.preferredSizes + size
            }
            currentState.copy(preferredSizes = updatedSizes)
        }
    }

    // Quitar método de pago
    fun onRemovePaymentMethod(methodId: String) {
        viewModelScope.launch {
            // TODO: Llamar al repositorio para eliminar
            _uiState.update { currentState ->
                currentState.copy(
                    paymentMethods = currentState.paymentMethods.filter { it.id != methodId }
                )
            }
        }
    }

    // Añadir método de pago
    fun onAddPaymentMethod() {
        // TODO: Navegar a pantalla de agregar tarjeta
        // Por ahora solo mock
        viewModelScope.launch {
            val newMethod = PaymentMethod(
                id = System.currentTimeMillis().toString(),
                lastFourDigits = "9999"
            )

            _uiState.update { currentState ->
                currentState.copy(
                    paymentMethods = currentState.paymentMethods + newMethod
                )
            }
        }
    }

    // Guardar cambios del perfil
    fun onSaveChanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingChanges = true, errorMessage = null) }

            // TODO: Llamar al repositorio para actualizar perfil
            delay(1000)

            _uiState.update {
                it.copy(
                    isSavingChanges = false,
                    successMessage = "Cambios guardados correctamente"
                )
            }

            // Limpiar mensaje de éxito después de 2 segundos
            delay(2000)
            _uiState.update { it.copy(successMessage = null) }
        }
    }

    // Cerrar sesión
    fun onLogout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }

            // TODO: Llamar al repositorio para cerrar sesión
            delay(800)

            _uiState.update { it.copy(isLoggingOut = false) }
            onSuccess()
        }
    }

    // Eliminar cuenta
    fun onDeleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // TODO: Mostrar diálogo de confirmación
            // TODO: Llamar al repositorio para eliminar cuenta
            delay(1000)
            onSuccess()
        }
    }

    // Cambiar idioma
    fun onChangeLanguage(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
        // TODO: Aplicar cambio de idioma globalmente
    }

    // Limpiar mensajes de error y éxito
    fun onDismissMessage() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    // Cargar datos del perfil (mock desde el repositorio)
    private fun loadProfileData() {
        viewModelScope.launch {
            // TODO: Llamada real al repositorio
            delay(300)

            _uiState.update {
                it.copy(
                    paymentMethods = listOf(
                        PaymentMethod("1", "1234"),
                        PaymentMethod("2", "5678")
                    ),
                    shippingAddresses = listOf(
                        ShippingAddress(
                            "1",
                            "DIRECCIÓN PRINCIPAL",
                            "Calle 45 #23-67, Laureles",
                            isPrimary = true
                        ),
                        ShippingAddress(
                            "2",
                            "DIRECCIÓN SECUNDARIA",
                            "Carrera 80 #34-12, Belén"
                        )
                    ),
                    preferredSizes = setOf("S", "M", "L")
                )
            }
        }
    }
}