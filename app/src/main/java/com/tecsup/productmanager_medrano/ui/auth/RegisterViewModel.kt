package com.tecsup.productmanager_medrano.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productmanager_medrano.core.utils.ResultState
import com.tecsup.productmanager_medrano.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val correo: String = "",
    val contrasenia: String = "",
    val confirmarContrasenia: String = "",
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val mensajeExito: String? = null
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(RegisterState())
    val estado: StateFlow<RegisterState> = _estado

    fun cambiarCorreo(valor: String) {
        _estado.value = _estado.value.copy(correo = valor)
    }

    fun cambiarContrasenia(valor: String) {
        _estado.value = _estado.value.copy(contrasenia = valor)
    }

    fun cambiarConfirmacion(valor: String) {
        _estado.value = _estado.value.copy(confirmarContrasenia = valor)
    }

    fun registrar(onExito: () -> Unit) {
        val e = estado.value
        val correo = e.correo.trim()
        val clave = e.contrasenia.trim()
        val confirmar = e.confirmarContrasenia.trim()

        if (correo.isEmpty() || clave.isEmpty() || confirmar.isEmpty()) {
            _estado.value = _estado.value.copy(mensajeError = "Completa todos los campos")
            return
        }

        if (clave != confirmar) {
            _estado.value = _estado.value.copy(mensajeError = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, mensajeError = null)

            when (val resultado = authRepository.registrarUsuario(correo, clave)) {
                is ResultState.Success -> {
                    _estado.value = _estado.value.copy(
                        cargando = false,
                        mensajeExito = "Registro exitoso, ahora inicia sesión"
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