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

    private lateinit var favoriteButton: ImageView
    private lateinit var filmTitle: TextView
    private lateinit var filmName: TextView
    private lateinit var filmYear: TextView
    private lateinit var backdrop: ImageView
    private lateinit var overwiew: TextView
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
        backdrop = view.findViewById(R.id.filmBackdrop)
        overwiew = view.findViewById(R.id.filmDescription)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        favoriteButton.setOnClickListener {
            toggleFavorite()
        }

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

    }

    private fun toggleFavorite() {
        val token = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        if (token.isEmpty()) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        isFavorite = !isFavorite
        updateFavoriteButton()

        val request = AuthModels.FavoriteRequest(movieId = movie.id.toInt())

        ApiClient.authApi.addToFavorites(token, request).enqueue(
            object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseText = response.body()?.string() ?: "Movie added to favorites"
                        Toast.makeText(context, responseText, Toast.LENGTH_SHORT).show()
                    } else {
                        revertFavoriteState()
                        val errorText = response.errorBody()?.string() ?: "Server error"
                        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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
            if (isFavorite) R.drawable.favorite_selected
            else R.drawable.favorite_not_selected
        )
    }
}
