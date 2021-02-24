package com.jaax.webservices.data

import com.jaax.webservices.model.Clima
import com.jaax.webservices.model.Lugar
import com.jaax.webservices.utils.Utils
import org.json.JSONException
import org.json.JSONObject

object JSONParser {

    fun getWeather( data: String ): Clima? {
        val clima = Clima()

        try {
            val jsonObject = JSONObject( data )
            val lugar = Lugar()

            val coordObject = Utils.getObject( "coord", jsonObject )
            lugar.latitud = Utils.getFloat( "lat", coordObject )
            lugar.longitud = Utils.getFloat( "lon", coordObject )

            val sysObject = Utils.getObject( "sys", jsonObject )
            lugar.pais = Utils.getString( "country", sysObject )
            lugar.lastUpdate = Utils.getLong( "dt", jsonObject )
            lugar.amanecer = Utils.getLong( "sunrise", sysObject )
            lugar.puestaSol = Utils.getLong( "sunset", sysObject )
            lugar.ciudad = Utils.getString( "name", jsonObject )
            clima.lugar = lugar

            val mainObject = Utils.getObject( "main", jsonObject )
            clima.condicionActual.humedad = Utils.getFloat( "humidity", mainObject )
            clima.condicionActual.temperatura = Utils.getDouble( "temp", mainObject )
            clima.condicionActual.presion = Utils.getFloat( "pressure", mainObject )
            clima.condicionActual.maxTemperatura = Utils.getFloat( "temp_max", mainObject )
            clima.condicionActual.minTemperatura = Utils.getFloat( "temp_min", mainObject )

            val jsonArray = jsonObject.getJSONArray( "weather" )
            val jsonWeather = jsonArray.getJSONObject( 0 ) //a partir de qué índice empieza a leer
            clima.condicionActual.id = Utils.getInt( "id", jsonWeather )
            clima.condicionActual.descripcion = Utils.getString( "description", jsonWeather )
            clima.condicionActual.condicion = Utils.getString( "main", jsonWeather )
            clima.condicionActual.icono = Utils.getString( "icon", jsonWeather )

            val windObject = Utils.getObject( "wind", jsonObject )
            clima.viento.velocidad = Utils.getFloat( "speed", windObject )
            clima.viento.gradosC = Utils.getFloat( "deg", windObject )

            val nubeObject = Utils.getObject( "clouds", jsonObject )
            clima.nube.precipitacion = Utils.getInt( "all", nubeObject )

            return clima
        } catch( jex: JSONException ) {
            jex.printStackTrace()
        }
        return null
    }
}