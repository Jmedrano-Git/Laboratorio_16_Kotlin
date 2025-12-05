package com.tecsup.productmanager_medrano.core.utils

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val category: String = "",
    val userId: String = "" // dueño del producto
)