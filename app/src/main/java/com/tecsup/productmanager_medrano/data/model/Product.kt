package com.tecsup.productmanager_medrano.data.model

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val urlImagen: String = "",
    val idUsuario: String = "" 
)
