package com.example.horoscopo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.data.HoroscopeProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.adapters.HoroscopeAdapter


class ListActivity : AppCompatActivity() {

    lateinit var horoscopeList: List<Horoscope>
    lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        horoscopeList = HoroscopeProvider.findAll()

        val adapter = HoroscopeAdapter(horoscopeList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



    }
}