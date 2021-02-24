package com.jaax.webservices.model

data class Lugar(
    var longitud: Float? = 0F,
    var latitud: Float? = 0F,
    var amanecer: Long? = 0,
    var puestaSol: Long? = 0,
    var pais: String? = null,
    var ciudad: String? = null,
    var lastUpdate: Long? = 0
)