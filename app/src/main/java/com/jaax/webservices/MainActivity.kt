package com.jaax.webservices

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jaax.webservices.data.HttpClient
import com.jaax.webservices.data.JSONParser
import com.jaax.webservices.databinding.ActivityMainBinding
import com.jaax.webservices.model.Clima
import com.jaax.webservices.utils.Utils
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var clima = Clima()
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate( layoutInflater )
        setContentView( bind.root )

        renderClimaDatos( "Dublin" )
    }

    private fun renderClimaDatos( ciudad: String ) {
        val climaTask = ClimaTask()
        climaTask.execute( "$ciudad&appid=${Utils.API_KEY}&units=metric" )
    }

    private inner class ClimaTask: AsyncTask<String, Void, Clima>() {
        override fun doInBackground(vararg params: String?): Clima {
            val httpData = HttpClient().getWeatherData( params[0] )
            Log.i( "httpdata:", httpData )
            clima = JSONParser.getWeather( httpData )!!

            clima.icon = clima.condicionActual.icono
            //ImagenAsync().execute( clima.icon )
            return clima
        }

        override fun onPostExecute(result: Clima?) {
            super.onPostExecute(result)

            val formato = DateFormat.getTimeInstance()
            val amanecer = formato.format( Date(clima.lugar!!.amanecer!!) )
            val puesta = formato.format( Date(clima.lugar!!.puestaSol!!) )
            val update = formato.format( Date(clima.lugar!!.lastUpdate!!) )

            val formatDec = DecimalFormat( "#.#" )
            val formatTemp = formatDec.format( clima.condicionActual.temperatura )

            bind.ciudad.text = "${clima.lugar!!.ciudad}, ${clima.lugar!!.pais}"
            bind.temperatura.text = "$formatTemp °C"
            bind.humedad.text = "Humedad: ${clima.condicionActual.humedad} %"
            bind.presion.text = "Presión: ${clima.condicionActual.presion}"
            bind.viento.text = "Viento: ${clima.viento.velocidad} m/s"
            bind.sunset.text = "Puesta sol: $puesta"
            bind.sunrise.text = "Amanecer: $amanecer"
            bind.update.text = "Última actualización: $update"
            bind.nube.text = "Nube: ${clima.condicionActual.condicion} ( ${clima.condicionActual.descripcion} )"
        }
    }

    private inner class ImagenAsync: AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap {
            return descargarImagen( params[0] as String )
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            bind.imagen.setImageBitmap( result )
        }

        private fun descargarImagen( codigo: String ): Bitmap {
            val cliente = DefaultHttpClient()
            val request = HttpGet( Utils.ICON_URL+codigo+".png" ) //según sea el clima, será la imagen

            try {
                val respuesta = cliente.execute( request )
                val status = respuesta.statusLine.statusCode

                if( status != HttpStatus.SC_OK ){
                    return null!!
                }
                val entidad = respuesta.entity
                if( entidad != null ){
                    val inputStream: InputStream?
                    inputStream = entidad.content

                    val bitmap: Bitmap = BitmapFactory.decodeStream( inputStream )
                    return bitmap
                }
            } catch( ioe: IOException ) {
                ioe.printStackTrace()
            }
            return null!!
        }
    }
}