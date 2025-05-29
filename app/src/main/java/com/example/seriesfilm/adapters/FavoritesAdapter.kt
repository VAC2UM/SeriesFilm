package com.example.seriesfilm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.AuthModels
import com.example.seriesfilm.R
import com.squareup.picasso.Picasso

class FavoritesAdapter(
    private var favorites: List<AuthModels.FavoriteItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.filmName)
        val posterImage: ImageView = itemView.findViewById(R.id.item_movie_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]
        holder.titleText.text = item.movieTitle

        // Загрузка постера с обработкой ошибок
        Picasso.get()
            .load(item.moviePoster)
            .fit()
            .centerCrop()
            .into(holder.posterImage)

        holder.itemView.setOnClickListener {
            onItemClick(item.movieId)
        }
    }

    override fun getItemCount(): Int = favorites.size

    fun updateFavorites(newFavorites: List<AuthModels.FavoriteItem>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}
