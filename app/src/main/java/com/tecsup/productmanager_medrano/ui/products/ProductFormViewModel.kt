package com.tecsup.productmanager_medrano.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productmanager_medrano.core.utils.ResultState
import com.tecsup.productmanager_medrano.data.model.Product
import com.tecsup.productmanager_medrano.data.repository.AuthRepository
import com.tecsup.productmanager_medrano.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductFormState(
    val id: String? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precioTexto: String = "",
    val stockTexto: String = "",
    val categoria: String = "",
    val urlImagen: String = "",
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val mensajeExito: String? = null
)

class ProductFormViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(ProductFormState())
    val estado: StateFlow<ProductFormState> = _estado

    fun cargarProducto(product: Product?) {
        if (product == null) {
            _estado.value = ProductFormState()
            return
        }
        _estado.value = _estado.value.copy(
            id = product.id,
            nombre = product.nombre,
            descripcion = product.descripcion,
            precioTexto = product.precio.toString(),
            stockTexto = product.stock.toString(),
            categoria = product.categoria,
            urlImagen = product.urlImagen
        )
    }

    fun cambiarNombre(valor: String)     { _estado.value = _estado.value.copy(nombre = valor) }
    fun cambiarDescripcion(valor: String){ _estado.value = _estado.value.copy(descripcion = valor) }
    fun cambiarPrecio(valor: String)     { _estado.value = _estado.value.copy(precioTexto = valor) }
    fun cambiarStock(valor: String)      { _estado.value = _estado.value.copy(stockTexto = valor) }
    fun cambiarCategoria(valor: String)  { _estado.value = _estado.value.copy(categoria = valor) }
    fun cambiarUrlImagen(valor: String)  { _estado.value = _estado.value.copy(urlImagen = valor) }

    fun guardarProducto(onExito: () -> Unit) {
        val e = estado.value

        if (e.nombre.isBlank() || e.precioTexto.isBlank() || e.stockTexto.isBlank() || e.categoria.isBlank()) {
            _estado.value = _estado.value.copy(mensajeError = "Completa todos los campos obligatorios")
            return
        }

        val precio = e.precioTexto.toDoubleOrNull()
        val stock = e.stockTexto.toIntOrNull()

        if (precio == null || stock == null) {
            _estado.value = _estado.value.copy(mensajeError = "Precio o stock no válidos")
            return
        }

        val idUsuario = authRepository.obtenerIdUsuarioActual() ?: run {
            _estado.value = _estado.value.copy(mensajeError = "Usuario no autenticado")
            return
        }

        val product = Product(
            id = e.id ?: "",
            nombre = e.nombre,
            descripcion = e.descripcion,
            precio = precio,
            stock = stock,
            categoria = e.categoria,
            urlImagen = e.urlImagen,
            idUsuario = idUsuario
        )

        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, mensajeError = null)

            val resultado = if (e.id == null) {
                productRepository.agregarProducto(product)
            } else {
                productRepository.actualizarProducto(product)
            }

            when (resultado) {
                is ResultState.Success -> {
                    _estado.value = ProductFormState(
                        mensajeExito = "Producto guardado correctamente"
                    )
                    onExito()
                }
                is ResultState.Error -> {
                    _estado.value = _estado.value.copy(
                        cargando = false,
                        mensajeError = resultado.message
                    )
                }
                ResultState.Loading -> Unit
            }
        }
    }
}