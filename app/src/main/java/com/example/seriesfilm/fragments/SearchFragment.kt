package com.example.seriesfilm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.Adapters.MoviesAdapter
import com.example.seriesfilm.MoviesRepository
import com.example.seriesfilm.R

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.resultFilmList)

        moviesAdapter = MoviesAdapter(emptyList())
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    MoviesRepository.fetchMovies(query, 1) { results, error ->
                        if (results != null) {
                            if (results.isNotEmpty()) {
                                moviesAdapter.updateMovies(results)
                            } else {
                                Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("SearchFragment", "Error occurred: $error")
                            Toast.makeText(context, error ?: "Unknown error", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: Handle text change if needed
                return false
            }
        })
        return view
    }
}