package com.jaax.webservices.model

data class CondicionActual(
    var id: Int = 0,
    var condicion: String? = null,
    var descripcion: String? = null,
    var icono: String? = null,
    var presion: Float = 0F,
    var humedad: Float = 0F,
    var maxTemperatura: Float = 0F,
    var minTemperatura: Float = 0F,
    var temperatura: Double = 0.toDouble()
)