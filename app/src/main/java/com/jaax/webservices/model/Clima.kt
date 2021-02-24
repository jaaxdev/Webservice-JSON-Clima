package com.jaax.webservices.model

import com.jaax.webservices.model.*

data class Clima(
    var lugar: Lugar? = null,
    var icon: String? = null,
    var condicionActual: CondicionActual = CondicionActual(),
    var temperatura: Temperatura = Temperatura(),
    var viento: Viento = Viento(),
    var nieve: Nieve = Nieve(),
    var nube: Nube = Nube()
)