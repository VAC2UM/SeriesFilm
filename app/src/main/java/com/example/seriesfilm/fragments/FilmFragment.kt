package com.example.seriesfilm.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seriesfilm.ApiClient
import com.example.seriesfilm.AuthModels
import com.example.seriesfilm.MoviesRepository
import com.example.seriesfilm.R
import com.example.seriesfilm.data.SearchResult
import com.example.seriesfilm.data.TitleDetailsResponse
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmFragment : Fragment() {
    lateinit var movie: SearchResult
    private var isFavorite = false
    private lateinit var titleDetails: TitleDetailsResponse

    private lateinit var favoriteButton: ImageView
    private lateinit var filmTitle: TextView
    private lateinit var filmName: TextView
    private lateinit var filmYear: TextView
    private lateinit var filmType: TextView
    private lateinit var backdrop: ImageView
    private lateinit var overwiew: TextView
    private lateinit var userRating: TextView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val ARG_MOVIE = "movie"
        private const val TOKEN_KEY = "auth_token"

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
        filmType = view.findViewById(R.id.filmType)
        backdrop = view.findViewById(R.id.filmBackdrop)
        overwiew = view.findViewById(R.id.filmDescription)
        userRating = view.findViewById(R.id.markIMDB)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        favoriteButton.setOnClickListener {
            toggleFavorite()
        }

        loadTitleDetails(movie.id)

        return view
    }

    private fun loadTitleDetails(titleId: Long) {
        MoviesRepository.fetchTitleDetails(titleId) { details, error ->
            if (details != null) {
                titleDetails = details
                updateUI(details)
                MoviesRepository.checkFavoriteStatus(titleId) { isFav ->
                    activity?.runOnUiThread {
                        isFavorite = isFav
                        updateFavoriteButton()
                    }
                }
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
        if (details.backdrop.isNotEmpty()) {
            Picasso.get()
                .load(details.backdrop)
                .into(backdrop)
        }

        filmTitle.text = details.title
        filmName.text = details.originalTitle
        overwiew.text = details.overwiew
        userRating.text = "Оценка зрителей: " + details.userRating
        filmYear.text = movie.year.toString()
        filmType.text = movie.type

        setRatingColor(details.userRating)
    }

    private fun setRatingColor(ratingString: String) {
        try {
            val numericRating = ratingString.substringBefore("/").toFloat()
            val color = when {
                numericRating < 4.0 -> R.color.rating_red
                numericRating < 7.0 -> R.color.rating_yellow
                else -> R.color.rating_green
            }
            userRating.setTextColor(resources.getColor(color, null))
        } catch (e: Exception) {
            userRating.setTextColor(resources.getColor(R.color.default_text_color, null))
        }
    }

    private fun toggleFavorite() {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        if (token.isEmpty()) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        isFavorite = !isFavorite
        updateFavoriteButton()

        val request = AuthModels.FavoriteRequest(
            movieId = movie.id.toInt(),
            movieTitle = movie.name,
            moviePoster = movie.imageUrl,
        )

        ApiClient.authApi.addToFavorites(token, request).enqueue(
            object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        val message = if (isFavorite) "Added to favorites" else "Removed from favorites"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                        // Обновляем список избранных через интерфейс
                        (parentFragmentManager.findFragmentByTag("favorites") as? FavoritesFragment)?.let {
                            it.loadFavorites()
                        }
                    } else {
                        revertFavoriteState()
                        val errorText = response.errorBody()?.string() ?: "Server error"
                        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {
                    revertFavoriteState()
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun revertFavoriteState() {
        isFavorite = !isFavorite
        updateFavoriteButton()
    }

    private fun updateFavoriteButton() {
        favoriteButton.setImageResource(
            if (isFavorite) {
                R.drawable.favorite_selected
            } else {
                R.drawable.favorite_not_selected
            },
        )
    }
}