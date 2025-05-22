package com.example.seriesfilm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seriesfilm.MoviesRepository
import com.example.seriesfilm.R
import com.example.seriesfilm.data.SearchResult
import com.example.seriesfilm.data.TitleDetailsResponse
import com.squareup.picasso.Picasso

class FilmFragment : Fragment() {
    lateinit var movie: SearchResult
    private lateinit var filmTitle: TextView
    private lateinit var filmName: TextView
    private lateinit var filmYear: TextView
    private lateinit var backdrop: ImageView
    private lateinit var overwiew: TextView

    companion object {
        private const val ARG_MOVIE = "movie"

        fun newInstance(movie: SearchResult): FilmFragment {
            val fragment = FilmFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getParcelable(ARG_MOVIE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_film, container, false)

        filmTitle = view.findViewById(R.id.filmTitle)
        filmName = view.findViewById(R.id.filmName)
        filmYear = view.findViewById(R.id.filmYear)
        backdrop = view.findViewById(R.id.filmBackdrop)
        overwiew = view.findViewById(R.id.filmDescription)

        loadTitleDetails(movie.id)

        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "Film: ${movie.name}", Toast.LENGTH_SHORT).show()
    }

    private fun loadTitleDetails(titleId: Long) {
        MoviesRepository.fetchTitleDetails(titleId) { details, error ->
            if (details != null) {
                // Обновляем UI с полученными данными
                updateUI(details)
            } else {
                Toast.makeText(
                    context,
                    "Ошибка загрузки: ${error ?: "Unknown"}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun updateUI(details: TitleDetailsResponse) {
        // Устанавливаем backdrop изображение
        if (details.backdrop.isNotEmpty()) {
            Picasso.get()
                .load(details.backdrop)
                .into(backdrop)
        }

        filmTitle.text = details.title
        filmName.text = details.title
        overwiew.text = details.overwiew
        filmYear.text = movie.year.toString()

//        filmPlot.text = details.plot
//        filmRating.text = "Rating: ${details.rating}/10"
//        filmRuntime.text = "Runtime: ${details.runtime} min"
//        filmGenres.text = "Genres: ${details.genres.joinToString(", ")}"
    }
}
