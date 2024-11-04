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
    lateinit var session: SessionManager
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val id = intent.getStringExtra("horoscope_id")!!


        horoscope = HoroscopeProvider.findById(id)

        //pongo la imagen y el nombre en su textview y en su imagenView
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


        findViewById<Button>(R.id.b).setOnClickListener {
            finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_details, menu)
        favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_favorite -> {
                if (isFavorite) {
                    session.setFavoriteHoroscope("")
                } else {
                    session.setFavoriteHoroscope(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                return true
            }
            R.id.menu_share -> {
              TODO()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFavoriteIcon () {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
        }
    }
}