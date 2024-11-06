package com.example.horoscopo.activities


import android.content.Intent
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
import android.view.Menu
import androidx.appcompat.widget.SearchView


class MainActivity : AppCompatActivity() {

    private lateinit var horoscopeList: List<Horoscope>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HoroscopeAdapter





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)


        // Configurar la Action Bar
        supportActionBar?.title = ""
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        horoscopeList = HoroscopeProvider.findAll()

        adapter = HoroscopeAdapter(horoscopeList) { position ->
            val horoscope = horoscopeList[position]
            navigateToDetail(horoscope)
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



    }




    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }

    private fun navigateToDetail(horoscope: Horoscope) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("horoscope_id", horoscope.id)
        startActivity(intent)
    }

    //MENU

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu) // Inflar el menú

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView // Obtener el SearchView

        // Configurar el SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Manejar la búsqueda al enviar la consulta es decir al dar al Enter(no haremos nada)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Manejar los cambios en el texto de búsqueda

                if (newText != null) {
                    horoscopeList = HoroscopeProvider.findAll().filter {
                        getString(it.name).contains(newText, true) ||
                                getString(it.description).contains(newText, true)
                    }
                    adapter.updateData(horoscopeList, newText)
                }
                return true
            }
        })

        return true
    }


}