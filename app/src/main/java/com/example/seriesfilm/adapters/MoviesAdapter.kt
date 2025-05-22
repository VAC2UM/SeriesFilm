package com.example.seriesfilm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.R
import com.example.seriesfilm.data.SearchResult
import com.squareup.picasso.Picasso

class MoviesAdapter(
    private var movies: List<SearchResult>,
    private var onItemClick: (SearchResult) -> Unit,
) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(
        itemView: View,
        private val onItemClick: (SearchResult) -> Unit,
    ) :
        RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        val filmName: TextView = itemView.findViewById(R.id.filmName)
        val filmYear: TextView = itemView.findViewById(R.id.filmYear)
        val filmType: TextView = itemView.findViewById(R.id.filmType)

        fun bind(movie: SearchResult) {
            itemView.setOnClickListener { (onItemClick(movie)) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        val movie = movies[position]
        Picasso.get().load(movie.imageUrl).into(holder.poster)
        Log.d("MoviesAdapter", "Film: ${movie.name}. Poster URL: ${movie.imageUrl}")
        holder.filmName.text = movie.name
        holder.filmYear.text = movie.year.toString()
        holder.filmType.text = movie.type
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<SearchResult>) {
        val filteredMovies =
            newMovies.filter { !it.imageUrl.isNullOrEmpty() && it.imageUrl != "https://cdn.watchmode.com/posters/blank.gif" }
        movies = filteredMovies
        notifyDataSetChanged()
    }
}
