package com.example.seriesfilm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.R
import com.example.seriesfilm.adapters.MoviesAdapter

class HomeFragment : Fragment() {
    private lateinit var testTextAPI: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        testTextAPI = view.findViewById(R.id.testTextAPI)
        recyclerView = view.findViewById(R.id.polpularFilmsList)

        moviesAdapter =
            MoviesAdapter(emptyList()) { movie ->
                // Переход на фрагмент с деталями фильма
                val fragment = FilmFragment.newInstance(movie)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return view
    }
}
