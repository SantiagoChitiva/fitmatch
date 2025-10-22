package com.example.fitmatch.presentation.ui.screens.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.FitMatchTheme
import com.example.fitmatch.R
import com.example.fitmatch.presentation.ui.screens.auth.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme

    // collectAsStateWithLifecycle respeta el ciclo de vida del Composable
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        containerColor = colors.background,
        topBar = {
            // ===== Header unificado: título centrado + back =====
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = colors.surface,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Back (izquierda)
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = colors.onSurface
                        )

                    }

                    // Título centrado (misma tipografía que el resto)
                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = colors.onSurface
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(inner)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // texto de apoyo
            Text(
                text = "Ingresa a tu cuenta y encuentra nuevas ofertas",
                style = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurfaceVariant),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            /* ------------------------ Email / Teléfono ------------------------ */
            OutlinedTextField(
                value = uiState.emailOrPhone, //lee del estado
                onValueChange = { viewModel.onEmailOrPhoneChanged(it) },
                label = { Text("Introduce tu cuenta") },
                placeholder = {
                    Text(
                        "Correo o número de teléfono",
                        color = colors.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                    focusedLabelColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(16.dp))

            /* ----------------------------- Password --------------------------- */
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Contraseña") },
                placeholder = { Text("Mínimo 8 caracteres", color = colors.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (uiState.showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = colors.onSurfaceVariant
                        )
                    }

                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                    focusedLabelColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(8.dp))

            // link “olvidé mi pass”
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.labelLarge.copy(color = colors.primary),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable {
                        viewModel.onForgotPasswordClick()
                        onForgotPasswordClick()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.weight(1f))
            //error
            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = colors.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.weight(1f))

            // Personaje "Tito" (imagen desde drawable)
            Image(
                painter = painterResource(id = R.drawable.guru),
                contentDescription = "Tito",
                modifier = Modifier.size(370.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.weight(1f))

            // Botón principal
            Button(
                onClick = {
                    viewModel.onLoginClick(onSuccess = onLoginSuccess) // ← Evento con callback
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState.isLoginEnabled && !uiState.isLoading, // ← Estado controla habilitación
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.surfaceVariant,
                    disabledContentColor = colors.onSurfaceVariant
                ),
                shape = MaterialTheme.shapes.large
            ) {
                if (uiState.isLoading) {
                    // Mostrar indicador de carga
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Continuar",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }}

// Con el heytitoTheme

@Preview(showBackground = true, showSystemUi = true, name = "Login – Light")
@Composable
private fun LoginScreenPreviewLight() {
    FitMatchTheme (darkTheme = false, dynamicColor = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login – Dark")
@Composable
private fun LoginScreenPreviewDark() {
    FitMatchTheme(darkTheme = true, dynamicColor = false) {
        LoginScreen()
    }
}