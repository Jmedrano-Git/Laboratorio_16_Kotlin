package com.tecsup.productmanager_medrano.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.tecsup.productmanager_medrano.core.utils.ResultState
import kotlinx.coroutines.tasks.await

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun registrarUsuario(correo: String, contrasenia: String): ResultState<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(correo, contrasenia).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error al registrar usuario")
        }
    }

    suspend fun iniciarSesion(correo: String, contrasenia: String): ResultState<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(correo, contrasenia).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
    }

    fun obtenerIdUsuarioActual(): String? = firebaseAuth.currentUser?.uid

    fun obtenerCorreoActual(): String? = firebaseAuth.currentUser?.email
}
