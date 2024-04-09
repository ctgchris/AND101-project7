package com.example.randompet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PikachuAdapter (private val pokemonName: List<String>, private val pokemonImg: List<String>, private val pokemonAbilities:List<List<String>>, private val pokemonType:List<String>) : RecyclerView.Adapter<PikachuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var Image: ImageView
         var Name: TextView
         var Type: TextView
         var Abilities: LinearLayout
        init {
            // Find our RecyclerView item's ImageView for future use
            Image = view.findViewById(R.id.pikachu_img)
            Name = view.findViewById(R.id.namePokemon)
            Type = view.findViewById(R.id.typePokemon)
            Abilities = view.findViewById(R.id.layoutAttribute)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pikachu_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(pokemonImg[position])
            .override(236,198)
            .into(holder.Image)
        holder.Name.text= pokemonName[position].uppercase()
        holder.Type.text= "Type: " + pokemonType[position]

        for (ability in pokemonAbilities[position]){
            val abilityTextView = TextView(holder.itemView.context)
            abilityTextView.text = ability
            abilityTextView.setTextAppearance(holder.itemView.context, R.style.attribute)
            holder.Abilities.addView(abilityTextView)
        }
        holder.Image.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${pokemonName[position].uppercase()} says Hi to you!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = pokemonImg.size
}