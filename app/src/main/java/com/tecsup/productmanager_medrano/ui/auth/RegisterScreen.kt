package com.tecsup.productmanager_medrano.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    volverALogin: () -> Unit
) {
    val estado = viewModel.estado.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::cambiarCorreo,
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.contrasenia,
                    onValueChange = viewModel::cambiarContrasenia,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.confirmarContrasenia,
                    onValueChange = viewModel::cambiarConfirmacion,
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                estado.mensajeError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                estado.mensajeExito?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = { viewModel.registrar(volverALogin) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estado.cargando
                ) {
                    if (estado.cargando) CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    ) else Text("Registrar")
                }

                TextButton(
                    onClick = volverALogin,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("¿Ya tienes cuenta? Iniciar sesión")
                }
            }
        }
    }
}