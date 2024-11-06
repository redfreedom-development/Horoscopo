package com.example.horoscopo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.R
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.utils.SessionManager


class HoroscopeAdapter(private var items: List<Horoscope>, val onItemClick: (Int) -> Unit): RecyclerView.Adapter<HoroscopeViewHolder>() {

    private var highlightText: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)
        return HoroscopeViewHolder(view)
    }
    //tamaño de la lista que he pasado con nombre items
    override fun getItemCount(): Int = items.size




    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = items[position]
        holder.render(horoscope)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }


}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {



    private var nameTextView: TextView = view.findViewById(R.id.nameTextView)
    private var datesTextView: TextView = view.findViewById(R.id.datesTextView)
    private var symbolImageView: ImageView = view.findViewById(R.id.symbolImageView)
    private var  favoriteImageView: ImageView

    init {

        nameTextView = view.findViewById(R.id.nameTextView)
        datesTextView = view.findViewById(R.id.datesTextView)
        symbolImageView = view.findViewById(R.id.symbolImageView)
        favoriteImageView = view.findViewById(R.id.favoriteImageView)

    }


    fun render(horoscope: Horoscope) {


        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        symbolImageView.setImageResource(horoscope.image)

        val context = itemView.context
        val isFavorite = SessionManager(context).isFavorite(horoscope.id)
        if (isFavorite) {
            favoriteImageView.visibility = View.VISIBLE
        } else {
            favoriteImageView.visibility = View.GONE
        }

    }

    // Este método sirve para actualizar los datos
    fun updateData (newDataSet: List<Horoscope>) {
        updateData(newDataSet, null)
    }

    fun updateData(newDataSet: List<Horoscope>, highlight: String?) {
        this.highlightText = highlight
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    // Subraya el texto que coincide con la busqueda
    fun highlight(text: String) {
        try {
            val highlighted = nameTextView.text.toString().highlight(text)
            nameTextView.text = highlighted
        } catch (e: Exception) { }
        try {
            val highlighted = descTextView.text.toString().highlight(text)
            descTextView.text = highlighted
        } catch (e: Exception) { }
    }
}