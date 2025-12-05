package com.tecsup.productmanager_medrano.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tecsup.productmanager_medrano.core.utils.Product
import com.tecsup.productmanager_medrano.core.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("productos")

    fun obtenerProductosPorUsuario(idUsuario: String) =
        callbackFlow<ResultState<List<Product>>> {
            trySend(ResultState.Loading)

            val listener = collection
                .whereEqualTo("idUsuario", idUsuario)
                .orderBy("nombre", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ResultState.Error(error.message ?: "Error al leer productos"))
                        return@addSnapshotListener
                    }

                    val lista = snapshot?.documents?.map { doc ->
                        doc.toObject(Product::class.java)!!.copy(id = doc.id)
                    }.orEmpty()

                    trySend(ResultState.Success(lista))
                }

            awaitClose { listener.remove() }
        }

    fun obtenerTodosLosProductos() =
        callbackFlow<ResultState<List<Product>>> {
            trySend(ResultState.Loading)

            val listener = collection
                .orderBy("nombre", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ResultState.Error(error.message ?: "Error al leer productos"))
                        return@addSnapshotListener
                    }

                    val lista = snapshot?.documents?.map { doc ->
                        doc.toObject(Product::class.java)!!.copy(id = doc.id)
                    }.orEmpty()

                    trySend(ResultState.Success(lista))
                }

            awaitClose { listener.remove() }
        }

    suspend fun agregarProducto(producto: com.tecsup.productmanager_medrano.data.model.Product): ResultState<Unit> {
        return try {
            collection.add(producto).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error al agregar producto")
        }
    }

    suspend fun actualizarProducto(producto: com.tecsup.productmanager_medrano.data.model.Product): ResultState<Unit> {
        return try {
            collection.document(producto.id).set(producto).await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error al actualizar producto")
        }
    }

    suspend fun eliminarProducto(idProducto: String): ResultState<Unit> {
        return try {
            collection.document(idProducto).delete().await()
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Error al eliminar producto")
        }
    }
}