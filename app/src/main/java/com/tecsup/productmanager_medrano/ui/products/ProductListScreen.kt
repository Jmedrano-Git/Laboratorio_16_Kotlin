package com.tecsup.productmanager_medrano.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.productmanager_medrano.data.model.Product

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    irAFormulario: (Product?) -> Unit
) {
    val estado = viewModel.estado.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { irAFormulario(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Mis productos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            when {
                estado.cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                estado.productos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tienes productos registrados")
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.productos.size) { index ->
                            val product = estado.productos[index]

                            ProductCardSimple(
                                product = product,
                                onEditar = { irAFormulario(product) },
                                onEliminar = { viewModel.eliminarProducto(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCardSimple(
    product: Product,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(product.nombre, fontWeight = FontWeight.Bold)
                if (product.descripcion.isNotBlank()) Text(product.descripcion)
                Text("Precio: S/ ${product.precio}")
                Text("Stock: ${product.stock}")
                Text("Categoría: ${product.categoria}")
            }

            Column {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
