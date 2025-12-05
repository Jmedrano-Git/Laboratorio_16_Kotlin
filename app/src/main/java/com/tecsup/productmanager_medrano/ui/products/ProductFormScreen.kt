package com.tecsup.productmanager_medrano.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ProductFormScreen(
    viewModel: ProductFormViewModel,
    volverALista: () -> Unit
) {
    val estado = viewModel.estado.collectAsState().value

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = if (estado.id == null) "Nuevo producto" else "Editar producto",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::cambiarNombre,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::cambiarDescripcion,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.precioTexto,
                onValueChange = viewModel::cambiarPrecio,
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.stockTexto,
                onValueChange = viewModel::cambiarStock,
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.categoria,
                onValueChange = viewModel::cambiarCategoria,
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.urlImagen,
                onValueChange = viewModel::cambiarUrlImagen,
                label = { Text("URL de imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            estado.mensajeError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            estado.mensajeExito?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = { viewModel.guardarProducto(volverALista) },
                modifier = Modifier.align(Alignment.End),
                enabled = !estado.cargando
            ) {
                if (estado.cargando)
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else
                    Text("Guardar")
            }
        }
    }
}