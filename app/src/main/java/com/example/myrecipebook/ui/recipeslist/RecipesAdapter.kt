package com.example.myrecipebook.ui.recipeslist

import coil.load
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.R
import com.example.myrecipebook.common.domain.model.Recipe
import androidx.core.net.toUri

class RecipesAdapter(
    private var items: List<Recipe> = emptyList(),
    private val onClick: (Recipe) -> Unit = {}
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    fun submitList(newItems: List<Recipe>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View, private val onClick: (Recipe) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val rating: TextView = itemView.findViewById(R.id.rating)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val tags: TextView = itemView.findViewById(R.id.tags)

        fun bind(item: Recipe) {
            title.text = item.name
            rating.text = "%.1f".format(item.rating)
            val totalMinutes = item.prepTimeMinutes + item.cookTimeMinutes
            time.text = "$totalMinutes min"
            tags.text = item.tags.joinToString(prefix = "#", separator = "  #")
            itemView.setOnClickListener { onClick(item) }
            image.load(item.image) {
                crossfade(true)
                placeholder(android.R.color.darker_gray)
                error(android.R.color.darker_gray)
            }
            // TODO: Adjust string format
        }
    }
}
