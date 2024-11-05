package com.example.horoscopo.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.data.HoroscopeProvider
import com.example.horoscopo.utils.SessionManager

class DetailActivity : AppCompatActivity() {

    private lateinit var horoscope: Horoscope
    private lateinit var favoriteMenuItem: MenuItem
    private lateinit var session: SessionManager
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

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


        findViewById<Button>(R.id.b).setOnClickListener {
            finish()
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
}