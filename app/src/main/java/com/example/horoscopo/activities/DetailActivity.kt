package com.example.horoscopo.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.data.HoroscopeProvider

class DetailActivity : AppCompatActivity() {

    lateinit var horoscope: Horoscope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val id = intent.getStringExtra("horoscope_id")!!

        horoscope = HoroscopeProvider.findById(id)

        findViewById<TextView>(R.id.tv).setText(horoscope.name)
        findViewById<ImageView>(R.id.iv).setImageResource(horoscope.image)
        findViewById<Button>(R.id.b).setOnClickListener {
            finish()
        }
    }
}