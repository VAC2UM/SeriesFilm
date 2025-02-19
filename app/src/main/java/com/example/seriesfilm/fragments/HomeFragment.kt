package com.example.seriesfilm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.seriesfilm.R

class HomeFragment : Fragment() {
    lateinit var testPoster: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        testPoster = view.findViewById(R.id.popularPosterFilm)

        testPoster.setOnClickListener {
            val filmFragment = FilmFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, filmFragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}