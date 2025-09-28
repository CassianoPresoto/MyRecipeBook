package com.example.myrecipebook.ui.recipeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myrecipebook.R
import com.example.myrecipebook.common.domain.model.Recipe

class RecipesAdapter(
    private val onClick: (Recipe) -> Unit = {}
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View, private val onClick: (Recipe) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val rating: TextView = itemView.findViewById(R.id.rating)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val tags: TextView = itemView.findViewById(R.id.tags)

        fun bind(item: Recipe) {
            title.text = item.name
            rating.text = itemView.context.getString(R.string.rating_format, item.rating)
            val totalMinutes = item.prepTimeMinutes + item.cookTimeMinutes
            time.text = itemView.context.getString(R.string.time_minutes_format, totalMinutes)
            tags.text = item.tags.joinToString(separator = "  ") {
                itemView.context.getString(R.string.tag_chip_format, it)
            }
            itemView.setOnClickListener { onClick(item) }
            image.load(item.image) {
                crossfade(true)
                placeholder(android.R.color.darker_gray)
                error(android.R.color.darker_gray)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem == newItem
        }
    }
}
