package com.example.fitmatch.presentation.ui.screens.vendedor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.FitMatchTheme
import kotlin.collections.minus
import kotlin.collections.plus
import kotlin.text.isNotBlank


data class SizeOption(
    val id: String,
    val name: String
)

data class ColorOption(
    val id: String,
    val name: String,
    val color: Color
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    onCloseClick: () -> Unit = {},
    onSaveDraftClick: () -> Unit = {},
    onPublishClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val shapes = MaterialTheme.shapes

    // usar saveable para no perder inputs al rotar / proceso muerto
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var sizeGuide by rememberSaveable { mutableStateOf("") }
    var labels by rememberSaveable { mutableStateOf("") }

    // sets de selección (ok con remember simple)
    var selectedSizes by remember { mutableStateOf(setOf("s")) }     // S marcada por defecto
    var selectedColors by remember { mutableStateOf(setOf("negro")) } // Negro marcado por defecto

    // catálogos
    val sizeOptions = listOf(
        SizeOption("xs", "XS"), SizeOption("s", "S"),
        SizeOption("m", "M"), SizeOption("l", "L"), SizeOption("xl", "XL")
    )
    val colorOptions = listOf(
        ColorOption("negro", "Negro", Color(0xFF424242)),
        ColorOption("blanco", "Blanco", Color(0xFFF5F5F5)),
        ColorOption("rojo", "Rojo", Color(0xFFF44336)),
        ColorOption("beige", "Beige", Color(0xFFF5F5DC))
    )

    // validación simple para publicar
    val canPublish = title.isNotBlank() && price.isNotBlank()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        //header
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colors.surface,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // título centro – usar onSurface para coherencia
                    Text(
                        text = "Crear publicación",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )
                    )

                    // botoncito cerrar (para descartar, navegar arriba o confirmar salida)
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = colors.onSurface
                        )
                    }
                }
            }
        }

        //selector fotos. aquí es para que hagan lo de la galeria
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) {
                        // placeholder de imagen – color suave para no tapar la UI
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    colors.surfaceVariant.copy(alpha = 0.6f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { /* TODO: abrir picker de imágenes */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar imagen",
                                tint = colors.onSurfaceVariant
                            )
                        }
                    }

                    // botoncito para “+” (agregar más imágenes)
                    Surface(
                        onClick = { /* TODO: abrir picker múltiple */ },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = colors.primary
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar más",
                                tint = colors.onPrimary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                //  texto en onSurfaceVariant para menor jerarquía
                Text(
                    text = "Sugerencia: agrega hasta 10 fotos y/o videos (carrusel)",
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        //campos para subir la prenda
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                LabeledField(label = "Título") {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                            focusedLabelColor = colors.primary,
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface
                        ),
                        shape = shapes.medium
                    )
                }

                // Descripción
                LabeledField(label = "Descripción") {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                            focusedLabelColor = colors.primary,
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface
                        ),
                        shape = shapes.medium
                    )
                }

                // Precio
                LabeledField(label = "Precio") {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                            focusedLabelColor = colors.primary,
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface
                        ),
                        shape = shapes.medium
                    )
                }

                // Tallas
                Column {
                    Text(
                        text = "Tallas",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = colors.onSurface
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sizeOptions) { size ->
                            val selected = size.id in selectedSizes
                            SizeChip(
                                size = size.name,
                                selected = selected,
                                onToggle = {
                                    selectedSizes = if (selected) selectedSizes - size.id else selectedSizes + size.id
                                }
                            )
                        }
                    }
                    // TODO: “botoncito” para abrir guía visual de tallas (modal)
                }

                // Colores
                Column {
                    Text(
                        text = "Colores",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = colors.onSurface
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(colorOptions) { colorOpt ->
                            val selected = colorOpt.id in selectedColors
                            ColorChip(
                                name = colorOpt.name,
                                swatch = colorOpt.color,
                                selected = selected,
                                onToggle = {
                                    selectedColors = if (selected) selectedColors - colorOpt.id else selectedColors + colorOpt.id
                                }
                            )
                        }
                    }
                }

                // Guía de tallas y etiquetas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LabeledField(label = "Guía de Tallas", modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = sizeGuide,
                            onValueChange = { sizeGuide = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primary,
                                unfocusedBorderColor = colors.outline,
                                cursorColor = colors.primary,
                                focusedLabelColor = colors.primary,
                                focusedContainerColor = colors.surface,
                                unfocusedContainerColor = colors.surface
                            ),
                            shape = shapes.medium
                        )
                    }
                    LabeledField(label = "Etiquetas", modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = labels,
                            onValueChange = { labels = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primary,
                                unfocusedBorderColor = colors.outline,
                                cursorColor = colors.primary,
                                focusedLabelColor = colors.primary,
                                focusedContainerColor = colors.surface,
                                unfocusedContainerColor = colors.surface
                            ),
                            shape = shapes.medium
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // TODO(uni): poner “botoncito” para escanear código de barras (auto llenar título/etiquetas)

                Spacer(Modifier.height(16.dp))

                /* -------------------------- Acciones finales -------------------------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Guardar borrador (Outlined para menor peso visual)
                    OutlinedButton(
                        onClick = onSaveDraftClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colors.primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = SolidColor(colors.outline)
                        ),
                        shape = shapes.large
                    ) {
                        Text(
                            text = "Guardar borrador",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Publicar
                    Button(
                        onClick = onPublishClick,
                        modifier = Modifier.weight(1f),
                        enabled = canPublish, // evitar publicar vacío
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary,
                            disabledContainerColor = colors.surfaceVariant,
                            disabledContentColor = colors.onSurfaceVariant
                        ),
                        shape = shapes.large
                    ) {
                        Text(
                            text = "Publicar",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

//composables

@Composable
private fun LabeledField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        content()
    }
}

@Composable
private fun SizeChip(
    size: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    // chip circular; si está seleccionado usamos primary con onPrimary
    Surface(
        onClick = onToggle,
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        color = if (selected) colors.primary else colors.surface,
        border = BorderStroke(
            1.dp,
            if (selected) colors.primary else colors.outline.copy(alpha = 0.6f)
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = size,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (selected) colors.onPrimary else colors.onSurface
                )
            )
        }
    }
    // TODO: si hay muchas tallas, meter scroll y botón "ver todas"
}

@Composable
private fun ColorChip(
    name: String,
    swatch: Color,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onToggle,
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface,
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) colors.primary else colors.outline.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // circulito del color (le pongo borde suave para que no “se pierda” en fondos claros)
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(swatch)
                    .border(1.dp, colors.outline.copy(alpha = 0.4f), CircleShape)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = colors.onSurface
                )
            )
        }
    }
    // TODO: botoncito “+” para crear color personalizado (picker)
}

//previews

@Preview(showBackground = true, showSystemUi = true, name = "Create Product – Light")
@Composable
private fun CreateProductPreviewLight() {
    FitMatchTheme(darkTheme = false, dynamicColor = false) {
        CreateProductScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Create Product – Dark")
@Composable
private fun CreateProductPreviewDark() {
    FitMatchTheme(darkTheme = true, dynamicColor = false) {
        CreateProductScreen()
    }
}
