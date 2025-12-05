package com.tecsup.productmanager_medrano.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productmanager_medrano.core.utils.ResultState
import com.tecsup.productmanager_medrano.data.model.Product
import com.tecsup.productmanager_medrano.data.repository.AuthRepository
import com.tecsup.productmanager_medrano.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProductListState(
    val productos: List<Product> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null
)

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(ProductListState())
    val estado: StateFlow<ProductListState> = _estado

    init {
        cargarProductosDelUsuario()
    }

    fun cargarProductosDelUsuario() {
        viewModelScope.launch {
            val idUsuario = authRepository.obtenerIdUsuarioActual() ?: return@launch

            productRepository
                .obtenerProductosPorUsuario(idUsuario)
                .collectLatest { resultado ->
                    when (resultado) {
                        is ResultState.Loading ->
                            _estado.value = _estado.value.copy(cargando = true)
                        is ResultState.Success ->
                            _estado.value = ProductListState(productos = resultado.datos)
                        is ResultState.Error ->
                            _estado.value = _estado.value.copy(
                                cargando = false,
                                mensajeError = resultado.mensaje
                            )
                    }
                }
        }
    }

    fun eliminarProducto(idProducto: String) {
        viewModelScope.launch {
            productRepository.eliminarProducto(idProducto)
            // el listener de Firestore actualiza la lista solo
        }
    }
}