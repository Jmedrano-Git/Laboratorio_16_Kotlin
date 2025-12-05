package com.tecsup.productmanager_medrano.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productmanager_medrano.core.utils.ResultState
import com.tecsup.productmanager_medrano.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val correo: String = "",
    val contrasenia: String = "",
    val cargando: Boolean = false,
    val mensajeError: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginState())
    val estado: StateFlow<LoginState> = _estado

    fun cambiarCorreo(valor: String) {
        _estado.value = _estado.value.copy(correo = valor)
    }

    fun cambiarContrasenia(valor: String) {
        _estado.value = _estado.value.copy(contrasenia = valor)
    }

    fun iniciarSesion(onExito: () -> Unit) {
        val correo = estado.value.correo.trim()
        val clave = estado.value.contrasenia.trim()

        if (correo.isEmpty() || clave.isEmpty()) {
            _estado.value = _estado.value.copy(mensajeError = "Completa tus datos")
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, mensajeError = null)

            when (val resultado = authRepository.iniciarSesion(correo, clave)) {
                is ResultState.Success -> {
                    _estado.value = _estado.value.copy(cargando = false)
                    onExito()     // aquí simplemente vas a la lista de productos
                }
                is ResultState.Error -> {
                    _estado.value = _estado.value.copy(
                        cargando = false,
                        mensajeError = resultado.mensaje
                    )
                }
                ResultState.Loading -> Unit
            }
        }
    }
}