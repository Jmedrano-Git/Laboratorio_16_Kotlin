package com.tecsup.productmanager_medrano.core.utils


// Esto lo hago para manejar estados de carga, éxito y error.
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
