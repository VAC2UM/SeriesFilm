package com.example.seriesfilm.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.Data.SearchResult
import com.example.seriesfilm.R
import com.squareup.picasso.Picasso

class MoviesAdapter(private var movies: List<SearchResult>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(item_movie: View) : RecyclerView.ViewHolder(item_movie) {
        val poster: ImageView = item_movie.findViewById(R.id.item_movie_poster)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        Picasso.get().load(movie.imageUrl).into(holder.poster)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<SearchResult>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}