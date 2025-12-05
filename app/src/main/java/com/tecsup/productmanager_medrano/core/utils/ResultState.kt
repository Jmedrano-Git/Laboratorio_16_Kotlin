package com.tecsup.productmanager_medrano.core.utils


// Esto lo hago para manejar estados de carga, éxito y error.
sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}
