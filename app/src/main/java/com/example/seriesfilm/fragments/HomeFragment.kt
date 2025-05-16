package com.example.seriesfilm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.Adapters.MoviesAdapter
import com.example.seriesfilm.R

class HomeFragment : Fragment() {
    lateinit var testTextAPI: TextView
    lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        testTextAPI = view.findViewById(R.id.testTextAPI)
        recyclerView = view.findViewById(R.id.polpularFilmsList)

        moviesAdapter = MoviesAdapter(emptyList())
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        return view
    }
}