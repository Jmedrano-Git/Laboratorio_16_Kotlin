package com.tecsup.productmanager_medrano.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.productmanager_medrano.data.repository.AuthRepository
import com.tecsup.productmanager_medrano.data.repository.ProductRepository

object AppContainer {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    val authRepository: AuthRepository by lazy {
        AuthRepository(firebaseAuth)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(firestore)
    }
}