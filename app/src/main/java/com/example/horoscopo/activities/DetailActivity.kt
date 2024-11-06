package com.example.horoscopo.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.data.HoroscopeProvider
import com.example.horoscopo.utils.SessionManager
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    private lateinit var horoscope: Horoscope
    private lateinit var favoriteMenuItem: MenuItem
    private lateinit var session: SessionManager
    private var isFavorite = false
    private lateinit var navigatonBarDetalles: NavigationBarView
    private lateinit var dailyHoroscopeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        dailyHoroscopeTextView=findViewById(R.id.dailyHoroscopeTextView)

        val id = intent.getStringExtra("horoscope_id")!!


        horoscope = HoroscopeProvider.findById(id)

        //pongo la imagen y el nombre en su textview y en su imagenView
        //nótese que esta forma hace lo mismo que crear una variable de tipo TextView o ImageView
        // y luego le asignas el findViewById de la caja de texto o de la img correspondiente y despues con esa variable
        //puedes usar sus funciones y propiedades en este caso setText o setImageResourde
        //CREO QUE ES MÁS ENTENDIBLE USAR VARIABLES PRIMERO A HACERLO  A MOGOLLON PUES PUEDE CONFUNDIR
        findViewById<TextView>(R.id.tv).setText(horoscope.name)
        findViewById<ImageView>(R.id.iv).setImageResource(horoscope.image)

        //al titulo del action bar es decir el menu de arriba
        // le pongo el nombre del simbolo del horoscopo elegido y un subtitulo
        //con las fechas del horoscopo elegido

        supportActionBar?.title= getText(horoscope.name)
        supportActionBar?.subtitle =getText(horoscope.dates)

        //esto pone una flechita para volver atrás aunque no es necesario ya
        //que para eso tenemos el atrás del movil de siempre
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //para saber si es favorito el horoscopo o no y meterlo en la sesion
        session = SessionManager(this)
        isFavorite = session.isFavorite(horoscope.id)

        //vamos a capturar el navigation bar de los botones del horoscopo y decirle que por defecto empiece en el diario
        navigatonBarDetalles=findViewById(R.id.navigationBarDetails)
        navigatonBarDetalles.selectedItemId= R.id.menu_daily

        navigatonBarDetalles.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_daily -> {
                    getHoroscopeLuck("daily")
                }
                R.id.menu_weekly -> {
                    getHoroscopeLuck("weekly")
                }
                R.id.menu_monthly -> {
                    getHoroscopeLuck("monthly")
                }
            }

            return@setOnItemSelectedListener true
        }



    }



    //esta funcion es propia al poner un activity bar(menu) y es necesaria para
    //mostrar ese actitvity bar (inflar se llama en lenguaje tecnico)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_details, menu)
        favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

   // esta funcion detecta el item seleccionado de un activity bar(menu)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                 true
            }
            R.id.menu_favorite -> {
                if (isFavorite) {
                    session.setFavoriteHoroscope("")
                } else {
                    session.setFavoriteHoroscope(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                 true
            }
            R.id.menu_share -> {
              return implementacionCompartir()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun implementacionCompartir(): Boolean {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.setType("text/plain")

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
        return true
    }

    private fun setFavoriteIcon () {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
        }
    }


    fun getHoroscopeLuck(method: String) {

        // Llamada en hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Declaramos la url
                val url = URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/$method?sign=${horoscope.id}&day=TODAY")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                // Preguntamos si hubo error o no
                if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                    // Metemos el cuerpo de la respuesta en un BurfferedReader
                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    // Parsear JSON
                    val json = JSONObject(response.toString())
                    val result =  json.getJSONObject("data").getString("horoscope_data")

                    // Ejecutamos en el hilo principal
                    /*CoroutineScope(Dispatchers.Main).launch {

                    }*/
                    runOnUiThread {
                        dailyHoroscopeTextView.text = result

                    }

                } else { // Hubo un error
                    Log.w("HTTP", "Response :: Hubo un error")
                }
            } catch (e: Exception) {
                Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
            }
        }
    }
}